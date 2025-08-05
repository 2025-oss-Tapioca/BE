package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.user.UserEntity;
import com.tapioca.BE.adapter.out.jpaRepository.UserJpaRepository;
import com.tapioca.BE.domain.port.out.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity findByLoginId(String loginId){
        return userJpaRepository.findByLoginId(loginId);
    }

    @Override
    public UserEntity save(UserEntity userEntity){
        return userJpaRepository.save(userEntity);
    }

    @Override
    public boolean existsByLoginId(String loginId){
        return userJpaRepository.existsByLoginId(loginId);
    }
}
