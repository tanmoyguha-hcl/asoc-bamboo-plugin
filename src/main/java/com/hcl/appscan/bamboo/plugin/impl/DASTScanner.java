/**
 * (c) Copyright IBM Corporation 2016.
 * (c) Copyright HCL Technologies Ltd. 2020.
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.bamboo.plugin.impl;

import com.atlassian.bamboo.build.artifact.ArtifactManager;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.core.util.FileUtils;
import com.hcl.appscan.bamboo.plugin.auth.BambooAuthenticationProvider;
import com.hcl.appscan.bamboo.plugin.util.ScanProgress;
import com.hcl.appscan.sdk.CoreConstants;
import com.hcl.appscan.sdk.logging.IProgress;
import com.hcl.appscan.sdk.results.NonCompliantIssuesResultProvider;
import com.hcl.appscan.sdk.scan.IScan;
import com.hcl.appscan.sdk.scanners.dynamic.DASTConstants;
import com.hcl.appscan.sdk.scanners.dynamic.DASTScanFactory;

import java.io.File;
import java.util.Map;

public class DASTScanner extends AbstractASoCScanner {
	private ProcessService processService;
	private BambooAuthenticationProvider authenticationProvider;
	private String jobId;

	public DASTScanner(LogHelper logger, ArtifactManager artifactManager, ProcessService processService) {
		super(logger, artifactManager);
		this.processService = processService;
	}

	@Override
	public String getScannerType() {
		return DASTConstants.DAST;
	}

	@Override
	public void scheduleScan(TaskContext taskContext) throws TaskException {
		logger.info("scan.schedule.dynamic");
		IProgress progress = new ScanProgress(logger);
		authenticationProvider = new BambooAuthenticationProvider(username, password);
		DASTScanFactory scanFactory = new DASTScanFactory();

		Map<String, String> scanProperties = getScanProperties(taskContext);
		IScan scan = scanFactory.create(scanProperties, progress, authenticationProvider);
		try {
			scan.run();
			jobId = scan.getScanId();
			logger.info("scan.schedule.success", jobId);
			provider = new NonCompliantIssuesResultProvider(scan.getScanId(), scan.getType(), scan.getServiceProvider(), progress);
			provider.setReportFormat(scan.getReportFormat());
			resultsRetriever = new ResultsRetriever(provider);
		} catch (Exception e) {
			logger.error("err.scan.schedule", e.getLocalizedMessage());
			throw new TaskException(e.getLocalizedMessage(), e.getCause());
		}
	}

	@Override
	protected Map<String, String> getScanProperties(TaskContext taskContext) {
		Map<String, String> properties = super.getScanProperties(taskContext);
		properties.put(CoreConstants.TARGET, taskContext.getConfigurationMap().get(CoreConstants.TARGET));
		return properties;
	}

	@Override
	public File initWorkingDir(TaskContext taskContext) throws TaskException {
		File workingDir = taskContext.getWorkingDirectory();
		File dirToScan = new File(workingDir, SA_DIR);

		if (dirToScan.exists())
			FileUtils.deleteDir(dirToScan);

		try {
			dirToScan.mkdirs();
		} catch (Exception e) {
			logger.error("err.working.dir.creation", e.getLocalizedMessage());
		}

		return dirToScan;
	}
}
