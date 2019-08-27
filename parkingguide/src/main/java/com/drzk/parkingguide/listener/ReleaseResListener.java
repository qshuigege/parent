package com.drzk.parkingguide.listener;

import com.drzk.parkingguide.camera.CameraCommunicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ReleaseResListener implements DisposableBean {

    private Logger log = LoggerFactory.getLogger(ReleaseResListener.class);

    @Override
    public void destroy() throws Exception {
        log.info("程序退出，释放资源。。。");
        CameraCommunicationUtil.cleanup();
    }
}
