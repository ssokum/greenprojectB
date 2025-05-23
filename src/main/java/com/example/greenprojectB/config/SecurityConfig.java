package com.example.greenprojectB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 사용자가 만든 로그인폼 적용하기
    http.formLogin(form -> form
            .loginPage("/member/memberLogin")
            .defaultSuccessUrl("/member/memberLoginOk", true)
            .failureUrl("/member/login/error")
            .usernameParameter("email")
            //.passwordParameter("pwd")
            .permitAll());

    // 각 페이지 접근권한설정
    http.authorizeHttpRequests(request -> request
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/member/accessPhoneNum").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/data/**").authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated());

    // 권한 없는 user의 접근시 예외처리
    http.exceptionHandling(exception -> exception
                    .accessDeniedPage("/error/accessDenied")
    );

    // 기본 로그아웃 처리
    http.logout(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
