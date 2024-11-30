package com.bazarboost.auth.config;

import com.bazarboost.auth.filter.JwtAuthenticationFilter;
import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.auth.service.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**"))
                // Agregar filtro JWT antes del filtro de autenticación de usuario/contraseña
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
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
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // Generar token JWT al iniciar sesión exitosamente
                            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                            String token = tokenProvider.generateToken(userDetails);

                            // Crear cookie con el token
                            Cookie cookie = new Cookie("jwt-token", token);
                            cookie.setHttpOnly(true);
                            cookie.setSecure(true); // Solo HTTPS
                            cookie.setPath("/");
                            cookie.setMaxAge((int) (jwtExpiration / 1000)); // Convertir ms a segundos

                            response.addCookie(cookie);
                            response.sendRedirect("/");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .addLogoutHandler((request, response, authentication) -> {
                            // Eliminar cookie JWT al cerrar sesión
                            Cookie cookie = new Cookie("jwt-token", null);
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        })
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