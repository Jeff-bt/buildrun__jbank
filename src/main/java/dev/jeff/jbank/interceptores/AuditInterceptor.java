package dev.jeff.jbank.interceptores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class AuditInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(AuditInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        return true; // O true quer dizer que manter o fluxo, se eu colocar false. Essa requisição não bate no controller
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        logger.info("Audit - Metodo: {}, Url: {}, StatusCode: {}, IpAddress: {}",
                request.getMethod(),
                request.getRequestURL(),
                response.getStatus(),
                request.getAttribute("x-user-ip")
        );
    }
}
