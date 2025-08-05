package com.tapioca.BE.domain.port.in.usecase.gpt;

import java.util.UUID;

public interface TrafficTestUseCase {
    public Object trafficTest(
            String type,String userInput, UUID teamId
    );
}