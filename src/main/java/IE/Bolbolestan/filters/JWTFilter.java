package IE.Bolbolestan.filters;

import IE.Bolbolestan.bolbolestanExceptions.TokenExpiredException;
import IE.Bolbolestan.middlewares.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JWTFilter implements Filter {
    public void destroy() {
    }

    private Boolean authenticationNotRequiredUrl(HttpServletRequest req) {
        return req.getRequestURI().equals("/signup")
                || req.getRequestURI().equals("/login")
                || req.getRequestURI().equals("/forgot-password")
                || req.getRequestURI().equals("/change-password");
    }

    private void setClaims(ServletRequest request,
                           ServletResponse response,
                           FilterChain chain,
                           String token)
            throws IOException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        try {
            Claims claims = Authentication.decodeJWT(token);
            req.setAttribute("studentId", claims.getId());
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.getWriter().write(
                    "{\"status\":\"expired\",\"message\":\"" + new TokenExpiredException().getMessage() + "\"}");
        } catch (JwtException e) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException,
            ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        if (authenticationNotRequiredUrl(req) || req.getMethod().equals("OPTIONS")) {

            chain.doFilter(request, response);
            return;
        }

        String token = req.getHeader("authorization");

        if (isEmpty(token)) {
            HttpServletResponse res = (HttpServletResponse)response;
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        setClaims(request, response, chain, token);
    }
}
