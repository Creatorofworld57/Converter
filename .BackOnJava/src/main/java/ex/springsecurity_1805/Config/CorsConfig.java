package ex.springsecurity_1805.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем источники (указываем конкретные домены)
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://130.193.62.14"));

        // Разрешаем заголовки, включая стандартные и кастомные
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-CUSTOM-HEADER"));

        // Разрешаем методы (включая OPTIONS для preflight-запросов)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Разрешаем отправку учётных данных (cookies и HTTP Authentication)
        config.setAllowCredentials(true);

        // Добавляем заголовки, которые клиент может прочитать
        config.setExposedHeaders(List.of("Authorization", "X-OTHER-CUSTOM-HEADER"));

        // Устанавливаем максимальный кеш времени для preflight-запросов
        config.setMaxAge(3600L); // 1 час

        // Применяем конфигурацию ко всем путям
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
