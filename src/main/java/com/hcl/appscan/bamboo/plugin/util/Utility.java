package com.hcl.appscan.bamboo.plugin.util;

import com.atlassian.bamboo.task.TaskContext;
import com.hcl.appscan.bamboo.plugin.impl.ISASTConstants;
import com.hcl.appscan.sdk.scanners.dynamic.DASTConstants;
import com.hcl.appscan.sdk.scanners.sast.SASTConstants;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utility {
	public static Map<String, String> getScanTypes() {
		Map<String, String> scanTypes = new HashMap<String, String>();
		scanTypes.put("", "");
		scanTypes.put(SASTConstants.STATIC_ANALYZER, SASTConstants.SAST);
		scanTypes.put(DASTConstants.DYNAMIC_ANALYZER, DASTConstants.DAST);
		return scanTypes;
	}

	public static Map<String, String> getScanOptions() {
		Map<String, String> scanOptions = new HashMap<String, String>();
		scanOptions.put(ISASTConstants.SCAN_OPTION_STAGING, ISASTConstants.SCAN_OPTION_STAGING);
		scanOptions.put(ISASTConstants.SCAN_OPTION_PRODUCTION, ISASTConstants.SCAN_OPTION_PRODUCTION);
		return scanOptions;
	}

	public static Map<String, String> getTestOptimizations() {
		Map<String, String> testOptimizations = new LinkedHashMap<String, String>();
		testOptimizations.put(ISASTConstants.OPTIMIZATION_FAST, ISASTConstants.OPTIMIZATION_FAST);
		testOptimizations.put(ISASTConstants.OPTIMIZATION_FASTER, ISASTConstants.OPTIMIZATION_FASTER);
		testOptimizations.put(ISASTConstants.OPTIMIZATION_FASTEST, ISASTConstants.OPTIMIZATION_FASTEST);
		testOptimizations.put(ISASTConstants.NO_OPTIMIZATION, ISASTConstants.NO_OPTIMIZATION);
		return testOptimizations;
	}

	public static String resolvePath(String path, TaskContext taskContext) {
		if (path != null && !(new File(path).isAbsolute()) && taskContext != null && taskContext.getWorkingDirectory().exists()) {
			return new File(taskContext.getWorkingDirectory(), path).getAbsolutePath();
		}
		return path;
	}
}
