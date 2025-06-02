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
                    .ignoringRequestMatchers("/company/**")
                    .ignoringRequestMatchers("/admin/**")
                    .ignoringRequestMatchers("/professional/**")
                    .ignoringRequestMatchers("/faq/**")
                    .ignoringRequestMatchers("/recruit/**")
                    .ignoringRequestMatchers("/notice/**")
                    .ignoringRequestMatchers("/ckeditor/**") // CKEditor 업로드 경로 제외
                    .ignoringRequestMatchers("/equipment/**")
                    .ignoringRequestMatchers("/equipmentImage/**")
                    // CSRF 토큰을 쿠키로 저장, HttpOnly 설정 비활성화
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
          .formLogin(form -> form
                  .loginPage("/member/memberLogin")
                  //.loginPage("/company/companyLogin")
                  .defaultSuccessUrl("/member/memberLoginOk", true)
                  //.defaultSuccessUrl("/company/companyLoginOk", true)
                  .failureUrl("/member/login/error")
                  //.failureUrl("/company/companyLogin/error")
                  .usernameParameter("memberId")
                  //.usernameParameter("companyId")
                  .permitAll());

    // 각 페이지 접근권한설정
    http.authorizeHttpRequests(request -> request
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/member/accessPhoneNum").permitAll()
            .requestMatchers("/", "/css/**", "/js/**").permitAll()
            .requestMatchers("/include/**", "/lib/**").permitAll()
            .requestMatchers("/member/memberJoin").permitAll()
            .requestMatchers("/board/**").permitAll()
            .requestMatchers("/faq/faqList").permitAll()
            .requestMatchers("/faqImage").permitAll()
            .requestMatchers("/recruit/**").permitAll()
            .requestMatchers("/writing/get_template_list").permitAll()
            .requestMatchers("/ckeditor/**").permitAll()
            .requestMatchers("/company/**").permitAll()
            .requestMatchers("/company/companyLogin").permitAll()
            .requestMatchers("/equipment/**").permitAll()
            .requestMatchers("/equipmentImage/**").permitAll()
            .requestMatchers("/notice/**").permitAll()
            .requestMatchers("/professional/**").hasRole("ADMIN") //나중에 바꾸기
            .requestMatchers("/recruit/recruitInput").hasRole("COMPANY")
            .requestMatchers("/notice/noticeWrite", "/notice/noticeUpdate","/faq/**","/equipment/**").hasRole("ADMIN")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated());

    // 권한 없는 user의 접근시 예외처리
    http.exceptionHandling(exception -> exception
                    .accessDeniedPage("/error/accessDenied")
    );

    // 기본 로그아웃 처리
    http.logout(Customizer.withDefaults());

    // Spring Security에 iframe 허용 설정 추가
    http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())); // same-origin 허용

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
