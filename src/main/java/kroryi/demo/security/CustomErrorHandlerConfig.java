package kroryi.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomErrorHandlerConfig {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorHandlerConfig.class);

    @Bean
    public HandlerExceptionResolver customErrorHandler() {
        return new HandlerExceptionResolver();
    }

    public class HandlerExceptionResolver {
        public ResponseEntity<Map<String, String>> handle404(NoHandlerFoundException ex) {
            Map<String, String> response = new HashMap<>();

            response.put("status", "404");

            response.put("error", "지정된 지원을 찾을 수가 없습니다.");

            response.put("message", ex.getMessage());

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        public ResponseEntity<Map<String, String>> handle403(AccessDeniedException ex) {
            Map<String, String> response = new HashMap<>();

            response.put("status", "403");

            response.put("error", "접근 권한이 없습니다.");

            response.put("message", ex.getMessage());

            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }
}
