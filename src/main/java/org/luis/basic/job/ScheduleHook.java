package org.luis.basic.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.luis.basic.common.ProcessContext;
import org.luis.basic.util.SpringContextFactory;
import org.luis.basic.util.StringUtils;
import org.springframework.stereotype.Component;


/**
 * 调度Hook,用于Job的调度实现，无UI的c-schedule
 * 
 * @description
 */
@Component("scheduleProcessHook")
public class ScheduleHook {

	private Logger logger = Logger.getLogger(getClass());

	public void onStart() {
		// 启动所有的注册的Job
		IQuartzService service = (IQuartzService) SpringContextFactory
				.getSpringBean(IQuartzService.KEY_SPRINGKEY);
		if (service != null) {
			// 启动调度服务
			service.start();

			ProcessContext context = ProcessContext.getInstance();
			// 读取config.properties配置文件schedule.job,获取Job信息
			String[] jobs = context
					.getStrPropValues(ProcessContext.PROPKEY_SCHEDULE_JOB);

			for (String jobParam : jobs) {
				// 每一个jobParam是以ID;ClassName;CronExpression为描述的,以分号隔开
				String[] jd = StringUtils.stringToArray(jobParam, ";");
				if (jd.length == 3) {
					// 解析Job信息，增加Job对象
					String jobId = jd[0];
					String jobClassName = jd[1];
					String jobCronExpression = jd[2];

					// 创建Job对象，缺省执行CRON表达式
					QuartzJob job = new QuartzJob(jobId, jobClassName,
							QuartzJob.TRIGGER_CRON);
					job.initCronTrigger(new Date(System.currentTimeMillis()),
							jobCronExpression);
					service.addJob(job);
					service.startJob(jobId);

					logger.info("job启动:ID=" + jobId + "\tClassName="
							+ jobClassName + "\tCron表达式=" + jobCronExpression);
				}
			}
		} else {
			logger.error("INgbfQuartzService is null! Can not find it from Spring Context..");
		}
	}
}
