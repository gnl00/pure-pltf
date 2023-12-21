package com.pure.listener;

import com.pure.classloader.Restarter;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * 应用每次启动都会发送 ApplicationEvent， 所以需要一个标记，来标记已经启动过了。
 */
// @Component
// 对于平常的 ApplicationListener 来说，只需要注入到 IOC 容器就可以了，
// 但是当前的 ApplicationListener 需要操作应用的生命周期，此时 IOC 还没启动，所以需要配置到 spring.factories 中进行加载
public class PltfApplicationListener implements ApplicationListener<ApplicationEvent>, Ordered {

    // 给最高的优先级
    private int order = Ordered.HIGHEST_PRECEDENCE;

    {
        System.out.println("PltfApplicationListener init");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 跳过第一个 ApplicationStartingEvent？
        if (event instanceof ApplicationStartingEvent startingEvent) {
            onApplicationStartingEvent(startingEvent);
        }
        if (event instanceof ApplicationPreparedEvent preparedEvent) {
            onApplicationPreparedEvent(preparedEvent);
        }
        if (event instanceof ApplicationReadyEvent || event instanceof ApplicationFailedEvent) {
            Restarter.getInstance().finish();
        }
        if (event instanceof ApplicationFailedEvent failedEvent) {
            onApplicationFailedEvent(failedEvent);
        }
    }

    private void onApplicationStartingEvent(ApplicationStartingEvent event) {
        String[] args = event.getArgs();
        Restarter.initialize(args);
    }

    private void onApplicationPreparedEvent(ApplicationPreparedEvent event) {
        Restarter.getInstance().prepare(event.getApplicationContext());
    }

    private void onApplicationFailedEvent(ApplicationFailedEvent failedEvent) {
    }

    @Override
    public int getOrder() {
        return order;
    }
}
