package com.wb.wrapper.interfaces;

import com.wb.wrapper.application.CusPrivateHandler;
import com.wb.wrapper.domain.BaseReq;
import com.wb.wrapper.domain.CusPrivateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wrapper")
public class WrapperController {

    @Autowired
    private CusPrivateHandler privateHandler;

    @RequestMapping("/cusPrivate")
    public void cusPrivate(){
        CusPrivateReq  privateReq = new CusPrivateReq();
        privateReq.setContent("这是对私的一个请求内容");
        privateHandler.process(privateReq);
    }
}
