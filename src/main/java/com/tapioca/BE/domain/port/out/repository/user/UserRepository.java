package com.tapioca.BE.domain.port.out.repository.user;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    public UserEntity findByLoginId(String loginId);
    public UserEntity save(UserEntity userEntity);
    public boolean existsByLoginId(String loginId);
}