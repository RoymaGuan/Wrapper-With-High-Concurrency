package com.wb.wrapper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@Slf4j
public abstract class AsyncBase {

    public static final int DEFAULT_QUEUE_SIZE = 200;
    public static final int DEFAULT_THREAD_NUMBER = 40;

    private LinkedBlockingQueue<BaseReq> queue;
    private ExecutorService executorService;
    private int threadNum;
    private int queueSize;

    public AsyncBase(int threadNum, int queueSize) {
        if(queueSize > 0){
            this.queueSize = queueSize;
        }else{
            this.queueSize = DEFAULT_QUEUE_SIZE;
        }
        this.queue = new LinkedBlockingQueue<>(this.queueSize);
        if(threadNum <= 0){
            this.threadNum = DEFAULT_THREAD_NUMBER;
        }else{
            this.threadNum = threadNum;
        }
        this.executorService = Executors.newFixedThreadPool(threadNum);
        log.info("异步器初始化成功");
    }

    protected abstract void add(BaseReq baseReq);
    protected abstract void process(BaseReq baseReq);
}
