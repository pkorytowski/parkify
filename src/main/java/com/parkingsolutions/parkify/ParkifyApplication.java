package com.parkingsolutions.parkify;

import com.parkingsolutions.parkify.auth.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@SpringBootApplication
public class ParkifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkifyApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable();

            //http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
            /*
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/login/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/register/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/parking/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/reservation/*").permitAll()
                    .anyRequest().authenticated();

             */
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.addAllowedOrigin("*");
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.setAllowedMethods(Arrays.asList(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()));
                corsConfiguration.setMaxAge(1800L);
                source.registerCorsConfiguration("/**", corsConfiguration); // you restrict your path here

                //UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            //source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
            return source;
        }
    }

    @EnableMongoRepositories("com.parkingsolutions.parkify.repository")
    @Configuration
    class MongoConfiguration {

    }
}
