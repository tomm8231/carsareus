package dat3.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dat3.security.error.CustomOAuth2AccessDeniedHandler;
import dat3.security.error.CustomOAuth2AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    System.out.println("CALLED");
    return new BCryptPasswordEncoder();
  }

  // Use this to fine tune the CORS headers, if needed (Not required for this semester)
  //@Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //This line is added to make the h2-console work (if needed)
    http.headers().frameOptions().disable();
    http
            .cors().and().csrf().disable()

            .httpBasic(Customizer.withDefaults())
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //REF: https://mflash.dev/post/2021/01/19/error-handling-for-spring-security-resource-server/
            .exceptionHandling((exceptions) -> exceptions
                    .authenticationEntryPoint(new CustomOAuth2AuthenticationEntryPoint())
                    .accessDeniedHandler(new CustomOAuth2AccessDeniedHandler())
            )
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(authenticationConverter());

    http.authorizeHttpRequests((authorize) -> authorize
            //Obviously we need to be able to login without being logged in :-)
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

        //create user without having a login
            .requestMatchers(HttpMethod.POST, "/api/members").permitAll()

            //Required in order to use the h2-console
            .requestMatchers("/h2*/**").permitAll()

            .requestMatchers("/").permitAll() //Allow for a default index.html file


            //necessary to allow for "nice" JSON Errors
            .requestMatchers("/error").permitAll()

            //.requestMatchers("/", "/**").permitAll());

           //.requestMatchers(HttpMethod.GET,"/api/demo/anonymous").permitAll());


           // Demonstrates another way to add roles to an endpoint

        /*
        //CARS

           .requestMatchers(HttpMethod.GET, "/api/cars").hasAuthority("ADMIN")

           .requestMatchers(HttpMethod.GET, "/api/cars/{id}").hasAuthority("ADMIN")
           //.requestMatchers(HttpMethod.GET, "/api/cars/{id}").hasAuthority("USER")
           //.requestMatchers(HttpMethod.GET, "/api/cars/{id}").hasAuthority("ANONYMOUS")

           .requestMatchers(HttpMethod.POST, "/api/cars").hasAuthority("ADMIN")

           .requestMatchers(HttpMethod.PUT, "/api/cars/{id}").hasAuthority("ADMIN")

           .requestMatchers(HttpMethod.PATCH, "/api/cars/best-discount/{id}/{value}").hasAuthority("ADMIN")

           .requestMatchers(HttpMethod.DELETE, "/api/cars/{id}").hasAuthority("ADMIN")

           .requestMatchers(HttpMethod.GET, "/api/cars/brand-model/{brand}/{model}").hasAuthority("ADMIN")
           //.requestMatchers(HttpMethod.GET, "/api/cars/brand-model/{brand}/{model}").hasAuthority("USER")
          // .requestMatchers(HttpMethod.GET, "/api/cars/brand-model/{brand}/{model}").hasAuthority("ANONYMOUS")

           .requestMatchers(HttpMethod.GET, "/api/cars/average-price-per-day").hasAuthority("ADMIN")
           //.requestMatchers(HttpMethod.GET, "/api/cars/average-price-per-day").hasAuthority("USER")
           //.requestMatchers(HttpMethod.GET, "/api/cars/average-price-per-day").hasAuthority("ANONYMOUS")

           .requestMatchers(HttpMethod.GET, "/api/cars/best-discount").hasAuthority("ADMIN")


        //MEDLEMMER
        .requestMatchers(HttpMethod.GET, "/api/members").hasAuthority("ADMIN")

        .requestMatchers(HttpMethod.GET, "/api/members/{username}").hasAuthority("ADMIN")

        //.requestMatchers(HttpMethod.POST, "/api/members").hasAuthority("ADMIN")
        //.requestMatchers(HttpMethod.POST, "/api/members").hasAuthority("USER")
        //.requestMatchers(HttpMethod.POST, "/api/members").hasAuthority("ANONYMOUS")

        .requestMatchers(HttpMethod.PUT, "/api/members/{username}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/members/{username}").hasAuthority("USER")

        .requestMatchers(HttpMethod.PATCH, "/api/members/ranking/{username}/{value}").hasAuthority("ADMIN")

        .requestMatchers(HttpMethod.DELETE, "/api/members/{username}").hasAuthority("ADMIN")

        .requestMatchers(HttpMethod.GET, "/api/members/with-reservation").hasAuthority("ADMIN")


        //RESERVATIONER

       // .requestMatchers(HttpMethod.GET, "/api/reservations").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/reservations").hasAuthority("USER")
       // .requestMatchers(HttpMethod.GET, "/api/reservations/{username}").hasAuthority("ADMIN")
       // .requestMatchers(HttpMethod.GET, "/api/reservations/{username}/count").hasAuthority("ADMIN")



        //for cars
        .requestMatchers(HttpMethod.GET, "/api/cars").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/averagePrice").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/notReserved").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/cars/{id}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/cars/{id}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/cars/{id}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/cars/{id}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/cars/{id}/{value}").hasAuthority("ADMIN")

        //for members
        .requestMatchers(HttpMethod.GET, "/api/members").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/members/{username}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/members/{username}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/members/ranking/{username}/{value}").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/members/{username}").hasAuthority("ADMIN")

        //for reservations
        .requestMatchers(HttpMethod.POST, "/api/reservations").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/reservations").hasAuthority("USER")
        .requestMatchers(HttpMethod.GET, "/api/reservations").hasAuthority("USER")
        .requestMatchers(HttpMethod.GET, "/api/reservations").hasAuthority("ADMIN")

*/


       .anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter authenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  /* Initialize static value "secret" */
  @Value("${app.secret-key}")
  private String secretKey;
  public static String tokenSecret;

  @Value("${app.secret-key}")
  public void setStaticValue(String secretKey) {
    SecurityConfig.tokenSecret = secretKey;
  }
  /* End of Initialize static value "secret" */

  @Bean
  public JwtEncoder jwtEncoder() throws JOSEException {
    System.out.println("1) ---> "+tokenSecret);
    SecretKey key = new SecretKeySpec(tokenSecret.getBytes(), "HmacSHA256");
    JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<SecurityContext>(key);
    return new NimbusJwtEncoder(immutableSecret);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    System.out.println("2) ---> "+tokenSecret);
    SecretKey originalKey = new SecretKeySpec(tokenSecret.getBytes(), "HmacSHA256");
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(originalKey).build();
    return jwtDecoder;
  }


  //TBD --> IS THIS THE RIGHT WAY
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
          throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }


}
