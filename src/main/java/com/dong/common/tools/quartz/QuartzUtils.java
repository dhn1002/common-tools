package com.dong.common.tools.quartz;

import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class QuartzUtils {

    private static Logger logger = LoggerFactory.getLogger(QuartzUtils.class);

    private static SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    private static Scheduler scheduler;

    private static String jobGroup = "myJobGroup";

    public static void createJob(String jobName, String cronExpression, Class<? extends Job> T) {
        try {
            scheduler = schedFact.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //构建cron
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            if (trigger == null) {
                //构建job
                JobDetail jobDetail = JobBuilder.newJob(T).withIdentity(jobName, jobGroup).build();
                //根据cron，构建一个CronTrigger
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();
            } else {
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                ((CronTriggerImpl) trigger).setStartTime(new Date());
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            logger.error("创建定时任务失败",e);
        }
    }

    /**
     * 关闭定时器
     */
    public static void shutDown() {
        try {
            scheduler.shutdown();
            Thread.sleep(1000);
        } catch (SchedulerException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
