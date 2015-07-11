package org.luis.basic.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;


/**
 * Quartz 的 Scheduler服务实现
 * 
 */
@Component(IQuartzService.KEY_SPRINGKEY)
public class QuartzServiceImpl implements IQuartzService {

	private final static Logger logger = Logger
			.getLogger(QuartzServiceImpl.class);

	/**
	 * 定义QuartzScheduler实例
	 */
	private Scheduler scheduler = null;

	/**
	 * 定义Job池
	 */
	private Map<String, QuartzJob> pool = new HashMap<String, QuartzJob>();
	
	public boolean isStarted(){
		try {
			return this.scheduler.isStarted();
		} catch (SchedulerException e) {
			throw new RuntimeException("启动调度失败," + e.getMessage());
		}
	}

	/**
	 * 启动Scheduler
	 */
	public void start() throws RuntimeException {
		try {
			this.scheduler.start();
		} catch (Exception e) {
			throw new RuntimeException("启动调度失败," + e.getMessage());
		}
	}

	/**
	 * 关闭Scheduler
	 */
	public void shutdown() throws RuntimeException {
		if (!pool.isEmpty()) {
			// TODO 需要判断池中的对象不存在，在
			throw new RuntimeException("关闭调度失败，仍有作业在池中");
		}

		try {
			this.scheduler.shutdown();
			this.pool.clear();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("关闭调度失败," + e.getMessage());
		}
	}

	// 加载JOB
	public void addJob(QuartzJob job) throws RuntimeException {
		if (pool.containsKey(job.getName())) {
			throw new RuntimeException("该作业已存在!" + job.getInfo());
		}
		// 注册,但不启动
		pool.put(job.getName(), job);
	}

	// 重置JOB
	public void resetJob(QuartzJob ngbfJob) throws RuntimeException {
		QuartzJob job = pool.get(ngbfJob.getName());
		if (job == null) {
			throw new RuntimeException("重置失败，该作业name不存在!");
		}

		if (job.getStatus() != QuartzJob.STATUS_UNEXECUTE) {
			throw new RuntimeException("重置失败，该作业还在运行中!");
		}
		// 重新注册,但不启动
		pool.put(job.getName(), ngbfJob);
	}

	// 执行JOB
	@Override
	public void startJob(String name) throws RuntimeException {
		QuartzJob job = pool.get(name);
		if (job == null) {
			throw new RuntimeException("该作业不存在!");
		}
		try {
			Trigger t = job.getTrigger();
			if(job.getTriggerType().equals(QuartzJob.TRIGGER_SIMPLE)){
				
				t.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
			}else{
				t.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
			}
			
			// 调度Job
			scheduler.scheduleJob(job.getJobDetail(), t);
			// 设置状态
			job.setStatus(QuartzJob.STATUS_EXECUTING);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("启动作业失败!" + job.getInfo());
		}
	}

	/**
	 * 
	 */
	@Override
	public void stopJob(String name) throws RuntimeException {
		QuartzJob job = pool.get(name);
		if (job == null) {
			throw new RuntimeException("该作业不存在!");
		}
		try {
			// 停止触发器
			scheduler.pauseTrigger(name, QuartzHelper.GROUP_TRIGGER);
			// 移除触发器
			scheduler.unscheduleJob(name, QuartzHelper.GROUP_TRIGGER);
			// 删除任务
			scheduler.deleteJob(name, QuartzHelper.GROUP_JOB);
			// 设置状态
			job.setStatus(QuartzJob.STATUS_UNEXECUTE);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("停止作业失败!" + job.getInfo());
		}
	}

	// 停止JOB
	public void pauseJob(String name) throws RuntimeException {
		QuartzJob job = pool.get(name);
		if (job == null) {
			throw new RuntimeException("该作业不存在!");
		}
		try {
			// 停止触发器
			scheduler.pauseTrigger(name, QuartzHelper.GROUP_TRIGGER);
			// 设置状态
			job.setStatus(QuartzJob.STATUS_PAUSING);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("停止作业失败!" + job.getInfo());
		}
	}

	// 恢复JOB
	@Override
	public void resumeJob(String name) {
		QuartzJob job = pool.get(name);
		if (job == null) {
			throw new RuntimeException("该作业不存在!");
		}
		try {
			// 恢复触发器
			scheduler.resumeTrigger(name, QuartzHelper.GROUP_TRIGGER);
			// 设置状态
			job.setStatus(QuartzJob.STATUS_EXECUTING);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("恢复作业失败!" + job.getInfo());
		}
	}

	// 去除Job
	@Override
	public void removeJob(String name) throws RuntimeException {
		QuartzJob job = pool.get(name);
		if (job == null) {
			throw new RuntimeException("该作业不存在!");
		}
		if (job.getStatus() != QuartzJob.STATUS_UNEXECUTE) {
			throw new RuntimeException("该作业还在执行中,不能去除!" + job.getInfo());
		}
		try {
			pool.remove(name);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("去除作业失败!" + job.getInfo());
		}
	}

	/**
	 * 增加GlobalListener
	 * 
	 * @since 1.6.0.0
	 * 
	 */
	@Override
	public void addGlobalListener(JobListener listener) {
		try {
			this.scheduler.addGlobalJobListener(listener);
		} catch (Exception e) {
			logger.error("addGlobalListener error:\t" + e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////

	public void showPool() {
		System.out.println("--------------------------------------");
		for (QuartzJob job : pool.values()) {
			logger.debug(job.getInfo());
		}
		System.out.println("--------------------------------------");
	}

	/**
	 * 构造函数，初始化scheduler对象，交由Spring來构造初始化
	 */
	public QuartzServiceImpl() {
		init();
	}

	private void init() {
		try {
			// 初始化scheduler
			if (this.scheduler == null || this.scheduler.isShutdown()) {
				this.scheduler = new StdSchedulerFactory().getScheduler();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 获取Job的下次触发时间
	 */
	@SuppressWarnings("unused")
	private Date getNextFireTime(String jobName) {
		try {
			return this.scheduler.getTrigger(jobName, jobName)
					.getNextFireTime();
		} catch (Exception e) {
			return null;
		}
	}
}
