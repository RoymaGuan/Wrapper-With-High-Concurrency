package com.wb.wrapper.infrastructure;

import com.wb.wrapper.domain.BaseReq;
import com.wb.wrapper.domain.BaseResp;

public interface Handler {

    void process(BaseReq baseReq);

    BaseResp transWatchCall(BaseReq baseReq);
}
