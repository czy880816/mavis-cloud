package com.laidian.common;

import com.laidian.common.propertysource.lazy.LazyRandomValuePropertySource;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 启动时机很早，不能通过component注入，在主程序启动时设置，否则监听不到ApplicationEnvironmentPreparedEvent
 */
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        LazyRandomValuePropertySource.addLazyRandomPortPSToEnvironment(applicationEnvironmentPreparedEvent.getEnvironment());
    }
}
