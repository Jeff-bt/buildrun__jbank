package dev.jeff.jbank.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final IpFilter ipfilter;

    public FilterConfig(IpFilter ipFilter) {
        this.ipfilter = ipFilter;
    }

    @Bean
    public FilterRegistrationBean<IpFilter> filterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<IpFilter>();

        registrationBean.setFilter(ipfilter);
        registrationBean.setOrder(0); //ordem de qual filtro será executado 1°(0) 2°(1)...
        //registrationBean.setUrlPatterns(); //Só executa esse filtro em x rota

        return registrationBean;
    }
}
