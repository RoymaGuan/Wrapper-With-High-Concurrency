package com.wb.wrapper.configuration;

import com.wb.wrapper.application.CusPrivateHandler;
import com.wb.wrapper.application.TransWatchImpl;
import com.wb.wrapper.infrastructure.ITransWatch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WrapperConfiguration {

    @Bean("transWatch")
    public ITransWatch transWatch(){
        return new TransWatchImpl();
    }

    @Bean
    public CusPrivateHandler cusPrivateHandler(@Qualifier("transWatch") ITransWatch  transWatch){
        return new CusPrivateHandler(transWatch,0,0);
    }
}
