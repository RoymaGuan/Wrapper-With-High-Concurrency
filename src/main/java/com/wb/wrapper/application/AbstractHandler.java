package com.wb.wrapper.application;

import com.wb.wrapper.domain.AsyncBase;
import com.wb.wrapper.domain.BaseReq;
import com.wb.wrapper.domain.BaseResp;
import com.wb.wrapper.infrastructure.Handler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractHandler implements Handler {


    private static final int DEFAULT_THREAD_NUMBER = 50; //这两个参数从数据库中动态加载
    private static final int DEFAULT_QUEUE_SIZE = 500;
    private static final long waitTimeOut = 1000;
    private int threadNum;
    private int queueSize;


    private volatile  ConcurrentHashMap<BaseReq, BaseResp> responseQueue = new ConcurrentHashMap<>();

    private final TransWatchCallTask transWatchCallTask;

    public AbstractHandler(int threadNum,int queueSize){
        if(threadNum <= 0){
            this.threadNum = DEFAULT_THREAD_NUMBER;
        }
        if(queueSize <= 0){
            this.queueSize = DEFAULT_QUEUE_SIZE;
        }
        this.transWatchCallTask = new TransWatchCallTask(this.threadNum,this.queueSize);
        new Thread(new LongPollTask(),"longPoll-Thread").start();
    }

    @Override
    public void process(BaseReq baseReq) {
        transWatchCallTask.add(baseReq);
        log.info("Req请求入队成功 : {}",baseReq);
        try {
            baseReq.getLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public abstract BaseResp transWatchCall(BaseReq baseReq);

    private class LongPollTask implements Runnable{

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    BaseReq req = transWatchCallTask.getQueue().take();
                    log.info("从队列里获取任务成功,开始处理请求Req : {}",req.getTraceId());
                    transWatchCallTask.process(req);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class TransWatchCallTask extends AsyncBase{

        public TransWatchCallTask(int threadNum, int queueSize) {
            super(threadNum, queueSize);
            log.info("TransWatchCallTask init Success!");
        }

        @Override
        protected void add(BaseReq baseReq) {
            boolean offer = getQueue().offer(baseReq);
            if(!offer){
                log.info("队列已满");
            }
        }

        @Override
        protected void process(BaseReq baseReq) {
            getExecutorService().execute(() ->{
                log.info("调用TransWatch接口方法");
                long start = System.currentTimeMillis();
                BaseResp baseResp = transWatchCall(baseReq);
                long end = System.currentTimeMillis();
                long expendTime = (end - start);
                if(expendTime >= waitTimeOut){
                    baseReq.setAvailable(false);
                }
                responseQueue.put(baseReq,baseResp);
                log.info("结果加入Response队列完成 ： {}",baseResp);
                synchronized (baseReq){
                    log.info("开始进行异步入库");
                    BaseResp resp = responseQueue.get(baseReq);
                    if(!baseReq.isAvailable()){
                        log.info("未超时记录入库 : {}",baseReq);
                    }else{
                        log.info("超时记录 : {}",baseReq);
                    }
                }
                baseReq.getLatch().countDown();
            });

        }
    }
}
