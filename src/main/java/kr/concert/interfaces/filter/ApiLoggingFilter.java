package kr.concert.interfaces.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ApiLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        filterChain.doFilter(request, response);

        long endTime = System.currentTimeMillis();
        log.info("📦 [{}] API 처리 시간: {} ms", uri, (endTime - startTime));
    }
}
