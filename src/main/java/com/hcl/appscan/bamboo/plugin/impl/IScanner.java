package com.hcl.appscan.bamboo.plugin.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.hcl.appscan.bamboo.plugin.util.Decrypt;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public interface IScanner extends ISASTConstants, IJSONConstants, IArtifactPublisher {
	public static final String SA_DIR 	= ".sa";

	public void setUsername(String username);

	public void setPassword(String password);

	public void setWorkingDir(File workingDir);

	public String getScannerType();

	public void scheduleScan(TaskContext taskContext) throws TaskException;

	public File initWorkingDir(TaskContext taskContext) throws TaskException;

	public void waitAndDownloadResult(TaskContext taskContext) throws TaskException, InterruptedException;

	public void waitForReady(TaskContext taskContext) throws TaskException, InterruptedException;

	public void downloadResult(TaskContext taskContext) throws TaskException;

	public long getHighCount();

	public long getMediumCount();

	public long getLowCount();
}
