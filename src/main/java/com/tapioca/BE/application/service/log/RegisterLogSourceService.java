package com.tapioca.BE.application.service.log;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import com.tapioca.BE.adapter.out.httpImpl.McpLogClient;
import com.tapioca.BE.adapter.out.mapper.LogRegisterMapper;
import com.tapioca.BE.application.dto.request.mcp.McpLogRegisterRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.port.in.usecase.log.RegisterLogSourceUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.db.DbRepository;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterLogSourceService implements RegisterLogSourceUseCase {

    private final BackRepository backRepository;
    private final FrontRepository frontRepository;
    private final DbRepository dbRepository;
    private final McpLogClient mcpLogClient;

    /** application.yml에 없으면 로컬 기본 콜백으로 사용됨 */
    @Value("${logging.callback-url:ws://localhost:18080/ws/log}")
    private String callbackUrl;

    @Override
    public void registerBackend(String teamCode) {
        // .orElseThrow()를 사용하여 Optional에서 엔티티를 바로 꺼내고, 없으면 예외를 던집니다.
        BackEntity e = backRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BACK));

        McpLogRegisterRequestDto dto = LogRegisterMapper.toMcpFromBack(e, teamCode, callbackUrl);
        mcpLogClient.registerLogSource(dto);
    }

    @Override
    public void registerFrontend(String teamCode) {
        // .orElseThrow()를 사용하여 Optional에서 엔티티를 바로 꺼내고, 없으면 예외를 던집니다.
        FrontEntity e = frontRepository.findByCode(teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRONT));

        McpLogRegisterRequestDto dto = LogRegisterMapper.toMcpFromFront(e, teamCode, callbackUrl);
        mcpLogClient.registerLogSource(dto);
    }

    @Override
    public void registerRds(String teamCode) {
        // .orElseThrow()를 사용하여 Optional에서 엔티티를 바로 꺼내고, 없으면 예외를 던집니다.
        DbEntity e = dbRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DB));

        McpLogRegisterRequestDto dto = LogRegisterMapper.toMcpFromDb(e, teamCode, callbackUrl);
        mcpLogClient.registerLogSource(dto);
    }
}
