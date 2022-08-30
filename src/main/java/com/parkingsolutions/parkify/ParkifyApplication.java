package com.parkingsolutions.parkify;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.parkingsolutions.parkify.auth.JWTAuthorizationFilter;
import com.parkingsolutions.parkify.mongo.converter.PointReadConverter;
import com.parkingsolutions.parkify.mongo.converter.PointWriteConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@SpringBootApplication
public class ParkifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkifyApplication.class, args);
    }

    @EnableWebSecurity
    //@Profile(value = {"development", "production"})
    @EnableSwagger2
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    //.antMatchers("/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/login/*").permitAll()
                    .antMatchers(HttpMethod.POST, "/register/*").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
                    //.antMatchers(HttpMethod.GET, "/reservation/*").authenticated()
                    //.antMatchers(HttpMethod.POST, "/parking/*").permitAll()
                    //.antMatchers(HttpMethod.POST, "/reservation/*").permitAll()
                    .anyRequest().authenticated();

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
/*
        @Override
        public void configure(WebSecurity webSecurity) {
            webSecurity.ignoring().antMatchers("/login/user");
        }

 */

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
    class MongoConfiguration extends AbstractMongoClientConfiguration {

        @Value("${spring.data.mongodb.uri}")
        private String mongoUri;

        @Value("${spring.data.mongodb.database}")
        private String database;

        @Override
        protected String getDatabaseName() {
            return database;
        }

        @Override
        protected void configureClientSettings(MongoClientSettings.Builder builder) {
            builder.applyConnectionString(new ConnectionString(mongoUri));
        }

        @Override
        protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter adapter) {
            adapter.registerConverter(new PointReadConverter());
            adapter.registerConverter(new PointWriteConverter());
        }

    }

    @EnableSwagger2
    @Configuration
    class SwaggerConfiguration{
        @Bean
        public Docket apiDocket() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.any())
                    .paths(PathSelectors.any())
                    .build();
        }
    }

}
