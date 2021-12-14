package com.societegenerale.accounts.configuration;

import com.societegenerale.accounts.security.AuthenticationFilter;
import com.societegenerale.accounts.service.impl.ClientServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ClientServiceImpl clientDetailsService;

    private final AuthenticationEntry authenticationEntry;

    public SecurityConfiguration(ClientServiceImpl clientDetailsService, AuthenticationEntry authenticationEntry) {
        this.clientDetailsService = clientDetailsService;
        this.authenticationEntry = authenticationEntry;
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(clientDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] patterns = new String[] {
            "/v2/api-docs/**",
            "/swagger.json",
            "/swagger-ui.html",
            "/api/authentication/**",
            "h2-console/**"
        };

        http
            .cors()
            .and()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(authenticationEntry)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers(patterns).permitAll()
                .antMatchers(HttpMethod.GET, "/operations/**").authenticated()
                .antMatchers("/operations/**").authenticated()
            ;
        http.headers().frameOptions().disable();
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
