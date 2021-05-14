package IE.Bolbolestan.filters;

import IE.Bolbolestan.middlewares.Authentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter implements Filter {
    public void destroy() {
    }


    private void invalidTokenException(ServletResponse response)
            throws IOException {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setStatus(401);
    }

    private Boolean authenticationNotRequiredUrl(HttpServletRequest req) {
        return req.getRequestURI().equals("/signup")
                || req.getRequestURI().equals("/login");
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
        } catch (Exception e) {
            e.printStackTrace();
            invalidTokenException(res);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException,
            ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if (authenticationNotRequiredUrl(req)) {
            chain.doFilter(request, response);
            return;
        }

        String token = req.getHeader("authorization");

        if (isEmpty(token)) {
            invalidTokenException(response);
            return;
        }

        setClaims(request, response, chain, token);
    }
}
