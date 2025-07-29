package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.domain.model.BackEnd;
import org.springframework.stereotype.Component;

@Component
public class BackEndMapper {
    public BackEnd toDomain(BackEntity backEntity){
        return new BackEnd(
                backEntity.getEc2Host(),
                backEntity.getAuthToken(),
                backEntity.getOs(),
                backEntity.getEnv(),
                backEntity.getEc2Url()
                );
    }
}
