package com.wb.wrapper.application;

import com.wb.wrapper.domain.BaseReq;
import com.wb.wrapper.domain.BaseResp;
import com.wb.wrapper.domain.HandlerType;
import com.wb.wrapper.infrastructure.ITransWatch;
import org.springframework.beans.factory.annotation.Autowired;

public class CusPrivateHandler extends AbstractHandler {

    private final  ITransWatch transWatch;

    private HandlerType handlerType  = HandlerType.CusPrivate;
    @Autowired
    public CusPrivateHandler(ITransWatch iTransWatch,int threadNum, int queueSize) {
        super(threadNum, queueSize);
        this.transWatch = iTransWatch;
    }

    public HandlerType getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(HandlerType handlerType) {
        this.handlerType = handlerType;
    }

    @Override
    public BaseResp transWatchCall(BaseReq baseReq) {
        return transWatch.transWatchCall(baseReq);
    }
}
