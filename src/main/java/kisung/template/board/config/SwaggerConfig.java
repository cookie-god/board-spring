package kisung.template.board.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정 파일을 읽기 위한 어노테이션
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
            )
        )
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("board swagger")
        .description("게시판 API 입니다.")
        .version("1.0.0");
  }
}
