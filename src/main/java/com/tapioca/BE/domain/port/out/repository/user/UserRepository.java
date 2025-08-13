package com.tapioca.BE.domain.port.out.repository.user;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository {
    public UserEntity findByUserId(String userId);
    public UserEntity save(UserEntity userEntity);
    public boolean existsByUserId(String userId);
}