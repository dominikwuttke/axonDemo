package com.example.axondemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .authorizeRequests()
////                .antMatchers( "/home").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .formLogin()
////                .loginPage("/login")
////                .permitAll()
////                .and()
////                .logout()
////                .permitAll();
//    }

//    @Bean
//    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf().disable()
//                .httpBasic().disable()
//                .build();
//    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("axon")
                        .password("axonuser")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

}
