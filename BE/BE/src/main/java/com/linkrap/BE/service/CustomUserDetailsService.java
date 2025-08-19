package com.linkrap.BE.service;

import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.UsersRepository;
import com.linkrap.BE.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Users user = usersRepository.findByLoginId(username)
                .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new CustomUserDetails(
                user.getUserId(),
                user.getLoginId(),
                user.getPasswordHash(),
                Collections.emptyList()
        );
    }
}
