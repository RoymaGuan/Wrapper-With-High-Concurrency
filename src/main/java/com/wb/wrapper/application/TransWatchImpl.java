package com.wb.wrapper.application;

import com.wb.wrapper.domain.BaseReq;
import com.wb.wrapper.domain.BaseResp;
import com.wb.wrapper.infrastructure.ITransWatch;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TransWatchImpl implements ITransWatch {

    private static final long timeOut = 3000;

    @Override
    public BaseResp transWatchCall(BaseReq req) {

        log.info("Start调用TransWatch筛查引擎");
        String respStr  = "返回对私具体结果，准备xml解析";
        BaseResp resp = new BaseResp(respStr);
        log.info("End 调用TransWatch筛查引擎");
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextLong(timeOut));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
