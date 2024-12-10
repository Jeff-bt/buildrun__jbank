package dev.jeff.jbank.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IpFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        var ipAddress = request.getRemoteAddr();

        request.setAttribute("x-user-ip", ipAddress); //Propaga para os outros filtros e requisições
        response.setHeader("x-user-ip", ipAddress);

        //DESATIVAR A APLICAÇÃO (não passa a request para frente)
        //response.setStatus(503);
        //E comenta o <chain.doFilter(request, response)> isso faz que el não passe para frente

        chain.doFilter(request, response);

    }
}
