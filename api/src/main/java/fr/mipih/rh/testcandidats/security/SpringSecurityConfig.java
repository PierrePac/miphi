package fr.mipih.rh.testcandidats.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private SecurityUserDetailService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.cors(cors -> cors.disable())
		.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						authorize -> authorize
							.requestMatchers("/login/admin", "/login/candidat").permitAll()
							.requestMatchers("/admin").hasRole("ADMIN")
							.requestMatchers("/user").hasRole("USER")
							.anyRequest().authenticated())
				.formLogin(
						form -> form
							.loginPage("http://localHost:4200")
							.successHandler(myAuthenticationSuccessHandler())
							.failureHandler(myAuthenticationFailureHandler())
							.permitAll());
		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.getWriter().write("connection ok");
			}
		};
	}

	@Bean
	public AuthenticationFailureHandler myAuthenticationFailureHandler(){
	    return new AuthenticationFailureHandler() {
	        @Override
	        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	                                            AuthenticationException exception) throws IOException, ServletException {
	            response.getWriter().write("connection fail"); 
	        }
	    };
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}
