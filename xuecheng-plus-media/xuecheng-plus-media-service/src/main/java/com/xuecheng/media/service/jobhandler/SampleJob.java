package com.xuecheng.media.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description 测试执行器
 */
@Component
@Slf4j
public class SampleJob {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("testJob")
    public void testJob() throws Exception {
        log.info("开始执行.....");

    }

    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();//执行器的序号，从0开始
        int shardTotal = XxlJobHelper.getShardTotal();//执行器的总数

        System.out.println("shardIndex="+shardIndex+",shardTotal="+shardTotal);

    }

}
