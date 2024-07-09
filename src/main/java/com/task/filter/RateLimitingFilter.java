package com.task.filter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RateLimitingFilter extends HttpFilter {

    private static final int SC_TOO_MANY_REQUESTS = 429;

    private final Bucket bucket;

    @Autowired
    public RateLimitingFilter(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getRequestURI().equals("/api/fetch-data")) {
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
            if (probe.isConsumed()) {
                response.addHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
                chain.doFilter(request, response);
            } else {
                response.setStatus(SC_TOO_MANY_REQUESTS);
                response.getWriter().write("Too many requests - try again later");
                return;
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
