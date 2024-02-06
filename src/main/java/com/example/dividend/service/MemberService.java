package com.example.dividend.service;

import com.example.dividend.entity.MemberEntity;
import com.example.dividend.model.Auth;
import com.example.dividend.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "can not find user -> " + username)
                );
    }

    public MemberEntity register(Auth.SignUp member){
        boolean exists =
                this.memberRepository.existsByUsername(member.getUsername());
        if(exists){
            throw new RuntimeException("이미 사용 중인 아이디 입니다.");
        }
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }

    public MemberEntity authenticate(Auth.SignIn member){
        MemberEntity user = this.memberRepository
                .findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다 "));

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        return user;
    }
}
