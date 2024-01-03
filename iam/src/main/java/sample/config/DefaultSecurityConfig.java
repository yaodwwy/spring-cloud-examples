/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import sample.federation.FederatedIdentityAuthenticationSuccessHandler;
import sample.service.UserService;

import java.util.UUID;

/**
 * @author Joe Grandja
 * @author Steve Riesenberg
 * @since 1.1
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final UserService userService;

    public DefaultSecurityConfig(JdbcUserDetailsManager jdbcUserDetailsManager, UserService userService) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.userService = userService;
    }

    // @formatter:off
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize ->
				authorize
					.requestMatchers("/assets/**", "/webjars/**", "/login", "/version").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin(formLogin ->
				formLogin
					.loginPage("/login"))
			.oauth2Login(oauth2Login ->
				oauth2Login
					.loginPage("/login")
					.successHandler(authenticationSuccessHandler())
			)
				.userDetailsService(userService)
		;

		http.csrf(AbstractHttpConfigurer::disable);
		http.httpBasic(Customizer.withDefaults());
		http.sessionManagement(AbstractHttpConfigurer::disable);

		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.NEVER));
		return http.build();
	}
	// @formatter:on

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new FederatedIdentityAuthenticationSuccessHandler();
    }

    // @formatter:off
	@Bean
	public UserDetailsService userDetailsService() {


		UserDetails user1 = User.withDefaultPasswordEncoder()
				.username("user1")
				.password("password")
				 .roles("USERX1")
				.build();

		UserDetails user2 = User.withDefaultPasswordEncoder()
				.username("user2")
				.password("password")
				.roles("USERX2")
				.build();

		jdbcUserDetailsManager.createUser(user1);
		jdbcUserDetailsManager.createUser(user2);

		return jdbcUserDetailsManager;
	}
	// @formatter:on


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
