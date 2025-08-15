package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.application.dto.response.server.ReadServerResponseDto;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.model.project.DB;
import com.tapioca.BE.domain.model.project.Front;
import org.springframework.stereotype.Component;

@Component
public class ServerMapper {

    public ReadServerResponseDto.FrontResponse toFrontResponse(Front f) {

        if (f == null) return null;

        return new ReadServerResponseDto.FrontResponse(
                f.getEc2Host(),
                f.getAuthToken(),
                f.getEntryPoint(),
                f.getOs(),
                f.getEnv(),
                f.getProtocol()
        );
    }

    public ReadServerResponseDto.BackResponse toBackResponse(BackEnd b) {

        if (b == null) return null;

        return new ReadServerResponseDto.BackResponse(
                b.getLoginPath(),
                b.getEc2Url(),
                b.getAuthToken(),
                b.getOs(),
                b.getEnv()
        );
    }

    public ReadServerResponseDto.DbResponse toDbResponse(DB d) {

        if (d == null) return null;

        return new ReadServerResponseDto.DbResponse(
                d.getDbAddress(),
                d.getDbUser(),
                d.getPassword(),
                d.getDbName(),
                d.getDbPort(),
                d.getRdsInstanceId(),
                d.getAwsRegion(),
                d.getRoleArn()
        );
    }
}
