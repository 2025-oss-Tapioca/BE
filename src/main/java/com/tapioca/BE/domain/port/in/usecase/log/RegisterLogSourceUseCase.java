package com.tapioca.BE.domain.port.in.usecase.log;

public interface RegisterLogSourceUseCase {
    void registerBackend(String teamCode);
    void registerFrontend(String teamCode);
    void registerRds(String teamCode);
}