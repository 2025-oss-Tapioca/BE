package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    public UserEntity findByUserId(String userId);
    public UserEntity save(UserEntity userEntity);
    public boolean existsByUserId(String userId);
}
