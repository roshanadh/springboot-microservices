package np.roshanadh.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
    serverHttpSecurity
            .csrf()
              .disable()
            .authorizeExchange(exchange -> exchange
                    // permit all requests for static resources for /eureka/**
                    .pathMatchers("/eureka/**")
                      .permitAll()
                    // authenticate all other requests
                    .anyExchange()
                      .authenticated())
            // enable OAuth2 resource server capabilities, and for the resource server, enable JWT capabilities
            .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

    return serverHttpSecurity.build();
  }
}
