package spring.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchApplication {
	// SpringApplication.run의 결과를 exit 메서드에 전달하여 배치 종료 상태 코드(0: 성공, 1: 실패 등)를 OS에 전달
	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(BatchApplication.class, args)));
	}
}
