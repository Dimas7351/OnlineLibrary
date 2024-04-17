package full.project.OnlineLibrary.config;

import full.project.OnlineLibrary.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true)
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // конфигурируем сам Spring Security
        // конфигурируем авторизацию
//        http.csrf(csrf -> csrf.disable()) // Отключаем защиту от межсайтовой подделки запросов
           http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/admin").hasRole("ADMIN") // Можно hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/auth/login", "/auth/registration","/error").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
               .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/books", true)
                        .failureUrl("/auth/login?error")) // передаст контроллер на представление
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))
                .authenticationManager(authenticationManager(http));

        return http.build();
    }

    // Настраиваем аутентификацию
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
                auth.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());

            return auth.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
