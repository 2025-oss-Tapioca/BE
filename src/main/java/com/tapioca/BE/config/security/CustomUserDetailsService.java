package com.tapioca.BE.config.security;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.domain.port.out.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId){
        UserEntity userEntity = userRepository.findByLoginId(loginId);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(
                userEntity.getId(),            // UUID
                userEntity.getLoginId(),        // 로그인 ID
                userEntity.getPassword(),
                authorities                    // 권한
        );
    }
}
