package com.fs.myerp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ResReleaseListener implements DisposableBean {

    private Logger log = LoggerFactory.getLogger(ResReleaseListener.class);

    @Override
    public void destroy() throws Exception {
        log.info("程序退出，释放资源。。。");
    }
}
