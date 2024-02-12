package dnd.donworry.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Server localServer = new Server();
		Server prodHttpsServer = new Server();

		localServer.setUrl("http://localhost:8080");
		localServer.setDescription("개발용 로컬 서버");
		prodHttpsServer.setUrl("https://donworry.online");
		prodHttpsServer.setDescription("운영용 배포 서버");

		return new OpenAPI()
			.components(new Components())
			.servers(List.of(localServer, prodHttpsServer))
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("API Test") // API의 제목
			.description("DonWorry Swagger UI") // API에 대한 설명
			.version("1.0.0"); // API의 버전
	}
}