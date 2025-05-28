package com.example.greenprojectB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // CSRF 토큰을 요청 속성에 설정하는 핸들러 생성
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    // 사용자가 만든 로그인폼 적용하기
    http
            .csrf(csrf -> csrf
                    // CSRF 토큰 요청 핸들러 설정
                    .csrfTokenRequestHandler(requestHandler)
                    // /register 경로에 대해 CSRF 보호 비활성화
                    .ignoringRequestMatchers("/register")
                    // CSRF 토큰을 쿠키로 저장, HttpOnly 설정 비활성화
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .formLogin(form -> form
            .loginPage("/member/memberLogin")
            .defaultSuccessUrl("/member/memberLoginOk", true)
            .failureUrl("/member/login/error")
            .usernameParameter("memberId")
            //.passwordParameter("pwd")
            .permitAll());

    // 각 페이지 접근권한설정
    http.authorizeHttpRequests(request -> request
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/member/accessPhoneNum").permitAll()
            .requestMatchers("/", "/css/**", "/js/**").permitAll()
            .requestMatchers("/member/memberJoin").permitAll()
            .requestMatchers("/member/memberLogin").permitAll()
            .requestMatchers("/data/**").authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated());

    // 권한 없는 user의 접근시 예외처리
    http.exceptionHandling(exception -> exception
                    .accessDeniedPage("/error/accessDenied")
    );

    // 기본 로그아웃 처리
    http.logout(Customizer.withDefaults());

    // CSRF 보호 기능을 활성화하고, CSRF 토큰을 쿠키에 저장하도록 구성
//    http
//            .csrf(csrf -> csrf
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            );
//    return http.build();

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
