package fr.mipih.rh.testcandidats.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.CorsFilter;

import fr.mipih.rh.testcandidats.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private AuthService authService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SessionSecurityContextRepository securityContextRepository;
	
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
		auth.authenticationProvider(authenticationProvider());

	}
	
	@Autowired
	private CorsFilter corsFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.addFilterBefore(corsFilter, ChannelProcessingFilter.class)
		.cors(cors -> cors.disable())
		.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						authorize -> authorize
							.requestMatchers("/login/admin", "/login/candidat").permitAll()
							.requestMatchers("/api/questions").hasRole("ADMIN")
							.requestMatchers("/admin").hasRole("ADMIN")
							.requestMatchers("/candidat").hasRole("CANDIDAT")
							.anyRequest().authenticated())
				.formLogin(
						form -> form
							.loginPage("http://localHost:4200")
							.successHandler(myAuthenticationSuccessHandler())
							.failureHandler(myAuthenticationFailureHandler())
							.permitAll())
				.securityContext(context -> context
						.securityContextRepository(securityContextRepository))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
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
		authProvider.setUserDetailsService(authService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
}
