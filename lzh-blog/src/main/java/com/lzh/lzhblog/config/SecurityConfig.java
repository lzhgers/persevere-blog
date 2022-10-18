package com.lzh.lzhblog.config;

import com.lzh.lzhblog.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/regist").permitAll()
                .antMatchers("/user/email/getCode/**").permitAll()
                .antMatchers("/user/email/**/**").permitAll()
                .antMatchers("/user/email/checkEmail").permitAll()
                .antMatchers("/user/rePassword").permitAll()
                .antMatchers("/user/logout").permitAll()
                .antMatchers("/article/pageListAll").permitAll()

                .antMatchers("/article/**/**").permitAll()
                .antMatchers("/category/**/**").permitAll()
                .antMatchers("/user/getUserByArticleId").permitAll()
                .antMatchers("/tag/**/**").permitAll()
                .antMatchers("/comment/article/countComment").permitAll()
                .antMatchers("/comment/commentList").permitAll()
                .antMatchers("/blog/getInfo").permitAll()
                .antMatchers("/carouselImg/**/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout().disable();
        http.cors();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
