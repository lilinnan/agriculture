package com.lln.agriculture.manage.security.config;

import com.lln.agriculture.manage.security.filter.JwtTokenVerifierFilter;
import com.lln.agriculture.manage.security.filter.JwtUsernamePasswordAuthenticationFilter;
import com.lln.agriculture.manage.security.handler.SimpleAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/9/1 20:08
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenVerifierFilter jwtTokenVerifierFilter;
    private final JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter;
    private final SimpleAccessDeniedHandler simpleAccessDeniedHandler;

    public WebSecurityConfig(JwtTokenVerifierFilter jwtTokenVerifierFilter, JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter, SimpleAccessDeniedHandler simpleAccessDeniedHandler) {
        this.jwtTokenVerifierFilter = jwtTokenVerifierFilter;
        this.jwtUsernamePasswordAuthenticationFilter = jwtUsernamePasswordAuthenticationFilter;
        this.simpleAccessDeniedHandler = simpleAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf()
                .disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtUsernamePasswordAuthenticationFilter)
                .addFilterAfter(jwtTokenVerifierFilter, JwtUsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(simpleAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * @return CorsConfigurationSource
     * @see <a href="https://juejin.im/post/6844903859689619470">Spring Boot与Vue跨域的问题</a>
     * @see <a href="https://www.wencst.com/archives/1635">Spring Boot 2.0.2+Ajax解决跨域请求的问题</a>
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");
        //是否发送Cookie信息
        config.setAllowCredentials(true);
        //放行哪些原始域(请求方式)
        config.addAllowedMethod("*");
        //放行哪些原始域(头部信息)
        config.addAllowedHeader("*");
        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
        config.addExposedHeader("Content-Type");
        config.addExposedHeader("X-Requested-With");
        config.addExposedHeader("accept");
        config.addExposedHeader("Origin");
        config.addExposedHeader("Access-Control-Request-Method");
        config.addExposedHeader("Access-Control-Request-Headers");
        config.setMaxAge(Long.MAX_VALUE);

        //2.添加映射路径
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return configSource;
    }
}
