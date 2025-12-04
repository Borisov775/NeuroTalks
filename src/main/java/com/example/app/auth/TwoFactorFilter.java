package com.example.app.auth;

import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TwoFactorFilter extends OncePerRequestFilter {

    private final UserRepo userRepo;

    public TwoFactorFilter(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        // allow 2fa endpoints and static resources
        if (path.startsWith("/api/2fa") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs") || path.startsWith("/static") || path.startsWith("/login") || path.startsWith("/reg")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            UserEntity user = userRepo.findByEmail(auth.getName());
            if (user != null && user.isTwoFactorEnabled()) {
                HttpSession session = request.getSession(false);
                boolean verified = session != null && Boolean.TRUE.equals(session.getAttribute("2fa_verified"));
                if (!verified) {
                    // redirect to 2FA UI (or return 401 for APIs)
                    if (path.startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"2fa_required\"}");
                        return;
                    } else {
                        response.sendRedirect("/2fa");
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
