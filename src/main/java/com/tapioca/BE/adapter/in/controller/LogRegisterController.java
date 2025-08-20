package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.log.RegisterLogSourceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 로그 소스 등록용 컨트롤러
 * - 팀코드 기반으로 백엔드/프론트/RDS 소스 등록을 트리거
 * - 응답은 CommonResponseDto를 사용하되, HTTP 상태코드는 ResponseEntity로 반영
 */
@RestController
@RequestMapping("/api/log/register")
@RequiredArgsConstructor
public class LogRegisterController {

    private final RegisterLogSourceUseCase useCase; // 서비스 유즈케이스(팀코드만 받음)

    /** CommonResponseDto 내 httpStatus를 실제 HTTP 응답 코드에 반영 */
    private ResponseEntity<CommonResponseDto<?>> respond(CommonResponseDto<?> body) {
        return ResponseEntity.status(body.httpStatus()).body(body);
    }

    @PostMapping("/backend/{teamCode}")
    public ResponseEntity<CommonResponseDto<?>> registerBackend(@PathVariable String teamCode) {
        // 팀코드로 DB 조회 → MCP 등록 (callbackUrl은 Service에서 설정값 사용)
        useCase.registerBackend(teamCode);
        return respond(CommonResponseDto.ok("Backend log register success"));
    }

    @PostMapping("/frontend/{teamCode}")
    public ResponseEntity<CommonResponseDto<?>> registerFrontend(@PathVariable String teamCode) {
        useCase.registerFrontend(teamCode);
        return respond(CommonResponseDto.ok("Frontend log register success"));
    }

    @PostMapping("/rds/{teamCode}")
    public ResponseEntity<CommonResponseDto<?>> registerRds(@PathVariable String teamCode) {
        useCase.registerRds(teamCode);
        return respond(CommonResponseDto.ok("RDS log register success"));
    }
}