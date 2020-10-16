package com.hcl.appscan.bamboo.plugin.util;

import com.hcl.appscan.bamboo.plugin.impl.LogHelper;
import com.hcl.appscan.sdk.logging.IProgress;
import com.hcl.appscan.sdk.logging.Message;

import java.io.Serializable;

public class ScanProgress implements IProgress, Serializable {
    private LogHelper logger;

    public ScanProgress(LogHelper logger) {
        this.logger = logger;
    }

    @Override
    public void setStatus(Message message) {
        setStatus(message , null);
    }

    @Override
    public void setStatus(Throwable throwable) {
        logger.error(throwable.getLocalizedMessage());
    }

    @Override
    public void setStatus(Message message, Throwable throwable) {
//        String m = message.getText() + (throwable != null ? ("\n" + throwable.getLocalizedMessage()) : "");
        String m = throwable == null ? logger.getText(message.getText()) : logger.getText(message.getText(), throwable);
        if (message.getSeverity() == Message.INFO) {
            logger.info(m);
        } else if (message.getSeverity() == Message.ERROR) {
            logger.error(m);
        } else logger.debug(m);
    }
}
