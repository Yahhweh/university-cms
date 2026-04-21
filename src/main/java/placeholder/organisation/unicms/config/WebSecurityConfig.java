package placeholder.organisation.unicms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import placeholder.organisation.unicms.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> {
                request.requestMatchers("/login", "/css/**", "/js/**", "/webjars/**", "/images/**").permitAll();
                request.anyRequest().authenticated();
            })
            .formLogin((form) -> form.loginPage("/login").permitAll().usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/", true).permitAll()).logout(LogoutConfigurer::permitAll)
            .rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret"));
        httpSecurity.exceptionHandling(ex -> ex.accessDeniedHandler((request, response, exception) -> {
            response.sendRedirect("/access-denied");
        }));
        httpSecurity.csrf(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService service, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(service);
        return provider;
    }
}
