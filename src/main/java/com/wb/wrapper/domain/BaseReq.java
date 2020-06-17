package com.wb.wrapper.domain;

import lombok.*;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BaseReq {
    String traceId = UUID.randomUUID().toString().replace("-","");
    CountDownLatch latch = new CountDownLatch(1);
    boolean isAvailable = true;
}
