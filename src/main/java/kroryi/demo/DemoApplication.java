package kroryi.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing // JPA 엔티티 자동 감사 활성화 기능 @Entity 지정된 생성일, 수정일 추적
@EnableJpaRepositories(basePackages = "kroryi.demo.repository")
// 레파지토리 인테페이스가 있는곳을 인식시켜 자동으로 생성 하도록 하는 설정 Scan 기능
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

}
