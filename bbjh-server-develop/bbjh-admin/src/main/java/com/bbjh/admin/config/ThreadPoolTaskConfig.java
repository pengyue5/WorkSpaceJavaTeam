/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fwb
 * @version V1.0.1
 * @Description
 * @date 2019/4/29  20:29
 */
@Configuration
public class ThreadPoolTaskConfig {

    @Bean
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //此方法返回可用处理器的虚拟机的最大数量; 不小于1
//        int core = Runtime.getRuntime().availableProcessors();
        int core = 10;
        executor.setCorePoolSize(core);//设置核心线程数
        executor.setMaxPoolSize(core*2 + 1);//设置最大线程数
        executor.setKeepAliveSeconds(3);//除核心线程外的线程存活时间
        executor.setQueueCapacity(40);//如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setThreadNamePrefix("thread-execute");//线程名称前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//设置拒绝策略
        return executor;
    }

}
