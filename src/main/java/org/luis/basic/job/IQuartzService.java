package org.luis.basic.job;

/**
 * 接口定义QuartzService服务接口
 * 
 * @author guoliang.li
 * @since 1.0.0
 */
public interface IQuartzService {

	public static String KEY_SPRINGKEY = "ngbfQuartzService";
	
	public boolean isStarted();

	/**
	 * 增加JOB，Job进入池中，但不执行
	 */
	public void addJob(QuartzJob ngbfJob) throws RuntimeException;
	/**
	 * 重新设置对象到池中,用于对象的信息发生变更，Key是不变的
	 */
	public void resetJob(QuartzJob ngbfJob) throws RuntimeException;
	/**
	 * 移除Job，Job一旦在执行，则不能删除，必须先停止
	 */
	public void removeJob(String name) throws RuntimeException;
	/**
	 * 启动JOB，放置到Schedule环境中
	 */
	public void startJob(String name) throws RuntimeException;
	/**
	 * 停止JOB，将Job从Schedule环境中移除
	 */
	public void stopJob(String name) throws RuntimeException;
	/**
	 * 启动JOB，放置到Schedule环境中
	 */
	public void pauseJob(String name) throws RuntimeException;
	/**
	 * 停止JOB，将Job从Schedule环境中移除
	 */
	public void resumeJob(String name) throws RuntimeException;
	/**
	 * 启动Scheduler
	 */
	public void start() throws RuntimeException;
	/**
	 * 关闭Scheduler
	 */
	public void shutdown() throws RuntimeException;
	/**
	 * 增加全局的监听器
	 */
	public void addGlobalListener(JobListener listener);
	/**
	 * 显示Pool中的信息
	 */
	public void showPool();
}
