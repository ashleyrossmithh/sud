package sud.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:4200"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        // 1. Разрешаем статику, чтобы сайт открывался
                        .requestMatchers("/", "/index.html", "/static/**", "/error", "/favicon.ico").permitAll()

                        // 2. Закрываем API — тут теперь точно спросит пароль
                        .requestMatchers("/api/entities/**").authenticated()

                        // 3. Всё остальное тоже под пароль
                        .anyRequest().authenticated()
                )
                // Включаем Basic Auth (логин/пароль в Postman и браузере)
                .httpBasic(org.springframework.security.config.Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Это уберет ошибку "Encoded password does not look like BCrypt"
        // Теперь пароль "admin123" будет работать как обычный текст
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        org.springframework.security.core.userdetails.UserDetails user =
                org.springframework.security.core.userdetails.User.withUsername("admin")
                        .password("admin123")
                        .roles("USER")
                        .build();

        return new org.springframework.security.provisioning.InMemoryUserDetailsManager(user);
    }
}