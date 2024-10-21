package ex.springsecurity_1805.Config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SameSiteCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Пропускаем остальные фильтры
        filterChain.doFilter(request, response);

        // Обрабатываем существующие куки
        String setCookieHeader = response.getHeader("Set-Cookie");
        if (setCookieHeader != null && (setCookieHeader.contains("JSESSIONID") || setCookieHeader.contains("remember-me"))) {
            // Добавляем SameSite=None и Secure к кукам
            response.setHeader("Set-Cookie", setCookieHeader + "; SameSite=None; Secure");
        }
    }


}
