package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.domain.model.BackEnd;
import org.springframework.stereotype.Component;

@Component
public class BackEndMapper {
    public BackEnd toDomain(BackEntity backEntity){
        return new BackEnd(
                backEntity.getLoginPath(),
                backEntity.getOs(),
                backEntity.getEnv(),
                backEntity.getEc2Url()
                );
    }
}
