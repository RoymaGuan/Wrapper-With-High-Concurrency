package com.wb.wrapper.infrastructure;

import com.wb.wrapper.domain.BaseReq;
import com.wb.wrapper.domain.BaseResp;

public interface ITransWatch {
    BaseResp transWatchCall(BaseReq req);
}
