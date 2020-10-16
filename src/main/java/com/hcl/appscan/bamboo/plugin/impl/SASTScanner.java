/**
 * (c) Copyright IBM Corporation 2016.
 * (c) Copyright HCL Technologies Ltd. 2020.
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.bamboo.plugin.impl;

import com.atlassian.bamboo.build.artifact.ArtifactHandlingUtils;
import com.atlassian.bamboo.build.artifact.ArtifactManager;
import com.atlassian.bamboo.plan.artifact.ArtifactDefinitionContext;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.hcl.appscan.bamboo.plugin.auth.BambooAuthenticationProvider;
import com.hcl.appscan.bamboo.plugin.util.ScanProgress;
import com.hcl.appscan.bamboo.plugin.util.Utility;
import com.hcl.appscan.sdk.CoreConstants;
import com.hcl.appscan.sdk.logging.IProgress;
import com.hcl.appscan.sdk.results.NonCompliantIssuesResultProvider;
import com.hcl.appscan.sdk.scan.IScan;
import com.hcl.appscan.sdk.scanners.dynamic.DASTScanFactory;
import com.hcl.appscan.sdk.scanners.sast.SASTConstants;
import com.hcl.appscan.sdk.scanners.sast.SASTScanFactory;
import com.hcl.appscan.sdk.utils.SystemUtil;
import org.apache.tools.ant.types.FileSet;
import com.atlassian.core.util.FileUtils;
import org.apache.tools.ant.types.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class SASTScanner extends AbstractASoCScanner {
	private ProcessService processService;
	private BambooAuthenticationProvider authenticationProvider;
	private String jobId;

	public SASTScanner(LogHelper logger, ArtifactManager artifactManager, ProcessService processService) {
		super(logger, artifactManager);
		this.processService = processService;
	}

	@Override
	public String getScannerType() {
		return SASTConstants.SAST;
	}

	private void setInstallDir() {
		if (SystemUtil.isWindows() && System.getProperty("user.home").toLowerCase().indexOf("system32")>=0) {
			System.setProperty(CoreConstants.SACLIENT_INSTALL_DIR, BAMBOO_APPSCAN_INSTALL_DIR.getPath());
		}
	}

	@Override
	public void scheduleScan(TaskContext taskContext) throws TaskException {
		logger.info("scan.schedule.static");
		IProgress progress = new ScanProgress(logger);
		authenticationProvider = new BambooAuthenticationProvider(username, password);
		SASTScanFactory scanFactory = new SASTScanFactory();

		Map<String, String> scanProperties = getScanProperties(taskContext);
		IScan scan = scanFactory.create(scanProperties, progress, authenticationProvider);
		try {
			setInstallDir();
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
		String target = taskContext.getConfigurationMap().get(CoreConstants.TARGET);
		if (target == null || target.trim().isEmpty()) {
			Collection<ArtifactDefinitionContext> artifacts = taskContext.getBuildContext().getArtifactContext().getDefinitionContexts();
			try {
				for (ArtifactDefinitionContext artifact : artifacts) {
					FileSet fileSet = ArtifactHandlingUtils.createFileSet(workingDir, artifact, true, null);
					for (Resource resource : fileSet) {
						target = resource.getLocation().getFileName();
						break;
					}
					if (target != null) break;
				}
			}
			catch (IOException e) {
			}
		}
		properties.put(CoreConstants.TARGET, Utility.resolvePath(target, taskContext));
		if (taskContext.getConfigurationMap().getAsBoolean("")) properties.put(CoreConstants.OPEN_SOURCE_ONLY, "");

		return properties;
	}

	@Override
	public File initWorkingDir(TaskContext taskContext) throws TaskException {
		File workingDir = taskContext.getWorkingDirectory();
		File dirToScan = new File(workingDir, SA_DIR);

		if (dirToScan.exists())
			FileUtils.deleteDir(dirToScan);

		dirToScan.mkdirs();

		Collection<ArtifactDefinitionContext> artifacts = taskContext.getBuildContext().getArtifactContext().getDefinitionContexts();

		if (artifacts.isEmpty())
			throw new TaskException(logger.getText("err.no.artifacts")); //$NON-NLS-1$

		try {
			for (ArtifactDefinitionContext artifact : artifacts) {
				logger.info("copy.artifact", artifact.getName(), dirToScan); //$NON-NLS-1$
				FileSet fileSet = ArtifactHandlingUtils.createFileSet(workingDir, artifact, true, null);
				ArtifactHandlingUtils.copyFileSet(fileSet, dirToScan);
			}
			return dirToScan;
		}
		catch (IOException e) {
			throw new TaskException(e.getLocalizedMessage(), e);
		}
	}
}
