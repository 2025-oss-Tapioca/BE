package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.log.BackendLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.log.FrontendLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.log.RdsLogSourceRegisterDto;
import com.tapioca.BE.application.service.log.LogRegisterService;
import com.tapioca.BE.config.common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * [로그 전용 등록 컨트롤러]
 * - 기존 back/front/db의 다른 기능 DTO/엔드포인트와 충돌하지 않도록 별도 경로 사용.
 * - 요청 바디는 새로 만든 log 패키지의 DTO를 사용한다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs/register")
public class LogRegisterController {

    private final LogRegisterService service;

    /** 백엔드 소스 등록 */
    @PostMapping("/backend")
    public CommonResponseDto<?> registerBackend(@RequestBody BackendLogSourceRegisterDto dto) {
        service.registerBackend(dto);
        return CommonResponseDto.created("백엔드 로그 수집 요청이 MCP에 등록되었습니다.");
    }

    /** 프론트 소스 등록 */
    @PostMapping("/frontend")
    public CommonResponseDto<?> registerFrontend(@RequestBody FrontendLogSourceRegisterDto dto) {
        service.registerFrontend(dto);
        return CommonResponseDto.created("프론트 로그 수집 요청이 MCP에 등록되었습니다.");
    }

    /** RDS 소스 등록 */
    @PostMapping("/rds")
    public CommonResponseDto<?> registerRds(@RequestBody RdsLogSourceRegisterDto dto) {
        service.registerRds(dto);
        return CommonResponseDto.created("RDS 로그 수집 요청이 MCP에 등록되었습니다.");
    }
}
