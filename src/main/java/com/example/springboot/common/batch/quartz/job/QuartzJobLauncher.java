package com.example.springboot.common.batch.quartz.job;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class QuartzJobLauncher extends QuartzJobBean {

	private static final Logger log = LoggerFactory.getLogger(QuartzJobLauncher.class);

	private String jobName;
	private JobLauncher jobLauncher;
	private JobLocator jobLocator;

	@Override
	protected void executeInternal(JobExecutionContext context) throws org.quartz.JobExecutionException {
		JobExecution jobExecution = null;
		try {
			Job job = jobLocator.getJob(jobName);
			
			Map<String, Object> jobDataMap = context.getMergedJobDataMap();
			
			JobParameters jobParameters = getJobParameters(jobDataMap);
			
			jobExecution = jobLauncher.run(job, jobParameters);
			
			log.info("{}_{} was completed successfully", job.getName(), jobExecution.getId());

		} catch (NoSuchJobException e) {
			log.error("Job {} not found", jobName);

		} catch (Exception e) {
			log.error("Error executing job: {}", jobName, e);
		}
	}

	protected JobParameters getJobParameters(Map<String, Object> jobDataMap) {
		JobParametersBuilder builder = new JobParametersBuilder();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			if (value instanceof String && !key.equals(jobName)) {
				builder.addString(key, (String) value);
			} else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number) value).longValue());
			} else if (value instanceof Date) {
				builder.addDate(key, (Date) value);
			} else {
				// JobDataMap contains values which are not job parameters
			}
		}
		
		builder.addLong("time", System.currentTimeMillis());
		
		return builder.toJobParameters();
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public JobLocator getJobLocator() {
		return jobLocator;
	}

	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}
}
