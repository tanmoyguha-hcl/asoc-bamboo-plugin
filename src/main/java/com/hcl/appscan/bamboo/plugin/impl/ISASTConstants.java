/**
 * (c) Copyright IBM Corporation 2016.
 * (c) Copyright HCL Technologies Ltd. 2020.
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.bamboo.plugin.impl;

import java.io.File;

public interface ISASTConstants {
	
	String SYS_BUILDER_PREFIX		= "system.builder.";	//$NON-NLS-1$
	String SA_CLIENT_UTIL_KEY 		= "saclientutil";		//$NON-NLS-1$
	
	String CFG_SELECTED_UTIL		= "selectedUtil";		//$NON-NLS-1$
	String CFG_SELECTED_CRED		= "selectedCred";		//$NON-NLS-1$
	String CFG_APP_ID				= "appId";				//$NON-NLS-1$
	String CFG_SUSPEND				= "suspendJob";			//$NON-NLS-1$
	String CFG_MAX_HIGH				= "maxHigh";			//$NON-NLS-1$
	String CFG_MAX_MEDIUM			= "maxMedium";			//$NON-NLS-1$
	String CFG_MAX_LOW				= "maxLow";				//$NON-NLS-1$
	String CFG_SEL_SCAN_TYPE		= "selectedScanType";	//$NON-NLS-1$

	Integer FAILED_RETRY_COUNT		= 5;					//$NON-NLS-1$
	Integer MIN_RETRY_INTERVAL		= 30;					//$NON-NLS-1$
	Integer DEFAULT_RETRY_INTERVAL	= 120;					//$NON-NLS-1$
	String REPORT_SUFFIX			= "_report";			//$NON-NLS-1$
	String APPSCAN_INTERVAL			= "APPSCAN_INTERVAL";	//$NON-NLS-1$
	String SCAN_OPTION_STAGING		= "Staging";			//$NON-NLS-1$
	String SCAN_OPTION_PRODUCTION	= "Production";			//$NON-NLS-1$

	String OPTIMIZATION_FAST		= "Fast";				//$NON-NLS-1$
	String OPTIMIZATION_FASTER		= "Faster";				//$NON-NLS-1$
	String OPTIMIZATION_FASTEST		= "Fastest";			//$NON-NLS-1$
	String NO_OPTIMIZATION			= "NoOptimization";		//$NON-NLS-1$

	File BAMBOO_APPSCAN_INSTALL_DIR	= new File(System.getProperty("user.dir"),".appscan"); //$NON-NLS-1$ //$NON-NLS-2$
}
