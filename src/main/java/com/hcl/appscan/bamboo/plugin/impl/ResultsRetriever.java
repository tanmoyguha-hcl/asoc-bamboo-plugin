package com.hcl.appscan.bamboo.plugin.impl;

import com.hcl.appscan.sdk.CoreConstants;
import com.hcl.appscan.sdk.results.IResultsProvider;

public class ResultsRetriever {
	private IResultsProvider provider;
	private String status;
	private String message;
	private int retryFailedCount;
	private int retryInterval;

	public ResultsRetriever(IResultsProvider provider) {
		this(provider, ISASTConstants.FAILED_RETRY_COUNT, ISASTConstants.MIN_RETRY_INTERVAL);
	}

	public ResultsRetriever(IResultsProvider provider, int retryFailedCount, int retryInterval) {
		this.provider = provider;
		this.status = "";
		this.message = "";
		this.retryInterval = Math.max(retryInterval, ISASTConstants.MIN_RETRY_INTERVAL);
		this.retryFailedCount = Math.max(retryFailedCount, ISASTConstants.FAILED_RETRY_COUNT);
	}

	public void setRetryFailedCount(int retryFailedCount) {
		this.retryFailedCount = retryFailedCount;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public void waitForResults() {
		retryInterval = Math.max(retryInterval, ISASTConstants.MIN_RETRY_INTERVAL);
		retryFailedCount = Math.max(retryFailedCount, ISASTConstants.FAILED_RETRY_COUNT);

		int failedCount = 0;
		while (failedCount < retryFailedCount) {
			boolean hasResults = provider.hasResults();
			status = provider.getStatus();
			message = provider.getMessage();

			if (hasResults) return;
			else if (status == null) {
				failedCount++;
			} else if (CoreConstants.FAILED.equalsIgnoreCase(status)) {
				break;
			} else failedCount = 0;

			try {
				Thread.sleep(retryInterval * 1000L);
			} catch (InterruptedException e) {
			}
		}

		status = CoreConstants.FAILED;
		message = com.hcl.appscan.sdk.Messages.getMessage(CoreConstants.ERROR_GETTING_DETAILS, " Consecutive failed retry count: " + failedCount);
	}

	public boolean hasFailed() {
		return (CoreConstants.FAILED.equalsIgnoreCase(status));
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
