package fr.mipih.rh.testcandidats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserAuthProvider userAuthProvider;
	private final UserAuthEntryPoint userAuthEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		List<RequestMatcher> requestMatchers = new ArrayList<>();

		//MvcRequestMatcher mvcRequestMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/login/admin", "/login/candidat", "/login/refresh-token");

		MvcRequestMatcher matcher1 = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/login/admin");
		matcher1.setMethod(HttpMethod.POST);
		requestMatchers.add(matcher1);

		MvcRequestMatcher matcher2 = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/login/candidat");
		matcher2.setMethod(HttpMethod.POST);
		requestMatchers.add(matcher2);

		MvcRequestMatcher matcher3 = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/login/refresh-token");
		matcher3.setMethod(HttpMethod.POST);
		requestMatchers.add(matcher3);

		OrRequestMatcher orRequestMatcher = new OrRequestMatcher(requestMatchers);


		http
				.exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthEntryPoint))
				.addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
				//TODO: csrf enable
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(orRequestMatcher).permitAll()
						.anyRequest().authenticated()
				);
		return http.build();
	}
}
