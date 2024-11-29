package com.bazarboost.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**"))  // Deshabilitar CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/registro", "/inicio-sesion", "/estaticos/**", "/estilos/**", "/js/**").permitAll()

                        // API REST
                        .requestMatchers("/api/usuarios/obtenerNombre").hasRole("CLIENTE")
                        .requestMatchers("/api/usuarios/perfil").hasRole("CLIENTE")
                        .requestMatchers("/api/usuarios/actualizar").hasRole("CLIENTE")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/ventas/**").hasRole("VENDEDOR")
                        .requestMatchers("/api/resenias/**").hasRole("CLIENTE")
                        .requestMatchers("/api/producto-carrito/**").hasRole("CLIENTE")
                        .requestMatchers("/api/productos/mis-productos").hasRole("VENDEDOR")
                        .requestMatchers("/api/productos/**").hasRole("CLIENTE")
                        .requestMatchers("/api/metodos-pago/**").hasRole("CLIENTE")
                        .requestMatchers("/api/facturas/**").hasRole("CLIENTE")
                        .requestMatchers("/api/direcciones/**").hasRole("CLIENTE")
                        .requestMatchers("/api/descuentos/**").hasRole("VENDEDOR")
                        .requestMatchers("/api/categorias").hasAnyRole("CLIENTE", "ADMINISTRADOR")
                        .requestMatchers("/api/categorias/**").hasRole("ADMINISTRADOR")

                        // MVC
                        .requestMatchers("/ventas/**").hasRole("VENDEDOR")
                        .requestMatchers("/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers("/usuarios/perfil").permitAll()
                        .requestMatchers("/productos/vendedor/**", "/productos/guardar").hasRole("VENDEDOR")
                        .requestMatchers("/productos", "/productos/detalle-producto/**").hasRole("CLIENTE")
                        .requestMatchers("/carrito/**").hasRole("CLIENTE")
                        .requestMatchers("/metodos-pago/**").hasRole("CLIENTE")
                        .requestMatchers("/facturas/**").hasRole("CLIENTE")
                        .requestMatchers("/direcciones/**").hasRole("CLIENTE")
                        .requestMatchers("/descuentos/**").hasRole("VENDEDOR")
                        .requestMatchers("/categorias/**").hasRole("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/productos");
                        })
                )
                .formLogin(form -> form
                        .loginPage("/inicio-sesion")
                        .loginProcessingUrl("/login")  // Spring Security procesará POST a /login
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/inicio-sesion")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

