package com.tapioca.BE.domain.port.out.repository.user;

import com.tapioca.BE.adapter.out.entity.user.UserEntity;

public interface UserRepository {
    public UserEntity findByLoginId(String loginId);
    public UserEntity save(UserEntity userEntity);
    public boolean existsByLoginId(String loginId);
}