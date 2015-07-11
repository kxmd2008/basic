package org.luis.basic.job;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * JOB实现
 * 
 */
public class QuartzJob {

	// JOB触发器类型
	public final static String TRIGGER_SIMPLE = "simple";
	public final static String TRIGGER_CRON = "cron";

	// 执行状态：未执行|执行中|暂停中(暂未支持)
	public final static int STATUS_UNEXECUTE = 0;
	public final static int STATUS_EXECUTING = 9;
	public final static int STATUS_PAUSING = 5;

	/**
	 * 触发类型
	 */
	private String triggerType = "";
	/**
	 * 触发器，根据触发器类型来设置
	 */
	private Trigger trigger = null;
	/**
	 * Job的Name，即Trigger的Name，唯一
	 */
	private String name = "";
	/**
	 * 定义Job是否已执行,缺省是不执行
	 */
	private int status = STATUS_UNEXECUTE;
	/**
	 * QuartzJob细节对象
	 */
	private JobDetail jobDetail = null;

	private SimpleTrigger triggerSimple = null;
	private CronTrigger triggerCron = null;

	/**
	 * 构造函数,指定唯一的name，Job实际的类名和触发器类型
	 * 
	 * @param name
	 * @param className
	 * @param triggerType
	 */
	public QuartzJob(String name, String className, String triggerType) {
		this.jobDetail = new JobDetail(name, QuartzHelper.GROUP_JOB,
				QuartzHelper.getJobClass(className));
		this.triggerType = triggerType;
		this.name = name;
		// 根据类型初始化Trigger对象
		if (triggerType.equals(TRIGGER_SIMPLE)) {
			triggerSimple = new SimpleTrigger(name,
					QuartzHelper.GROUP_TRIGGER);
			this.trigger = triggerSimple;
		} else if (triggerType.equals(TRIGGER_CRON)) {
			triggerCron = new CronTrigger(name, QuartzHelper.GROUP_TRIGGER);
			this.trigger = triggerCron;
		} else {

		}
	}

	/**
	 * 初始化简单的触发器
	 */
	public void initSimepleTrigger(Date startTime, long repeatInterval,
			int repeatCount) throws RuntimeException {
		if (!triggerType.equals(TRIGGER_SIMPLE)) {
			throw new RuntimeException("初始化失败,触发器类型不匹配!");
		}

		triggerSimple.setStartTime(startTime);
		triggerSimple
				.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
		triggerSimple.setRepeatInterval(repeatInterval);
		triggerSimple.setRepeatCount(repeatCount);
	}

	/**
	 * 初始化Cron触发器
	 * 
	 * @param startTime
	 * @param cronExpression
	 * @throws NgbfException
	 */
	public void initCronTrigger(Date startTime, String cronExpression)
			throws RuntimeException {
		if (!triggerType.equals(TRIGGER_CRON)) {
			throw new RuntimeException("初始化失败,触发器类型不匹配!");
		}

		boolean tof = QuartzHelper.isValidCronExpression(cronExpression);
		if (!tof) {
			throw new RuntimeException("Cron表达式不正确!");
		}

		try {
			triggerCron.setStartTime(startTime);
			triggerCron.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
			triggerCron.setCronExpression(cronExpression);
			triggerCron.setTimeZone(TimeZone.getDefault());
		} catch (ParseException e) {
			throw new RuntimeException("初始化失败," + e.getMessage());
		}
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// public String getJobClassName() {
	// return jobClassName;
	// }
	//
	// public void setJobClassName(String jobClassName) {
	// this.jobClassName = jobClassName;
	// }

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public String getInfo() {
		return this.getName() + "-" + this.getTriggerType() + "-"
				+ this.getStatus();
	}

}
