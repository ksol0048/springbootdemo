package kroryi.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kroryi.demo.security.CustomErrorHandlerConfig;
import kroryi.demo.service.CustomUserDetailsService;
import kroryi.demo.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomerSecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final DataSource dataSource;
    private final OAuth2UserService oAuth2UserService;

    @Autowired
    private CustomErrorHandlerConfig.HandlerExceptionResolver customErrorHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("------------------보안설정----------------");

        http
                .csrf(csrf -> csrf.disable())
                .rememberMe(me -> me
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(60 * 60 * 24 * 30))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/member/login", "/member/join").permitAll() // permitAll() 모든 사이트 허가
//                        .anyRequest().authenticated()) //모든 요청에 대한 인증 필요, 모든 사이트 다 막고 시작
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/member/login") //로그인 페이지로 이동
                        .defaultSuccessUrl("/board/list",true)
//                        이것을 생략하면 http://localhost:8080/
                        .permitAll()) //모든 사용자에 로그인 페이지 접그 허용
                .oauth2Login(login->login.loginPage("/member/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint()
                        .userService(oAuth2UserService))
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler()))
                .logout(LogoutConfigurer::permitAll);
        // 6.1 바뀐 부분 기존에는 http.formLogin().loginPage

        return http.build();
    }

    /*@Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }*/

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ResponseEntity<Map<String, String>> responseEntity = customErrorHandler.handle403(accessDeniedException);

            response.setStatus(responseEntity.getStatusCode().value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
        };
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User=(DefaultOAuth2User) authentication.getPrincipal();

//            String id = defaultOAuth2User.getAttributes().get("id").toString();
//            String body = """
//                    {"id":"%s"}
//                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            response.sendRedirect("/");
//            PrintWriter writer = response.getWriter();
//            writer.println(body);
//            writer.flush();
        });
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("-----------웹 환경 설정-------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }
}
