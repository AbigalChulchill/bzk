package net.bzk.auth;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.bzk.auth.aop.JwtAuthenticationEntryPoint;
import net.bzk.auth.aop.JwtRequestFilter;
import net.bzk.auth.security.oauth.CustomOAuth2UserService;
import net.bzk.auth.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import net.bzk.auth.security.oauth.OAuth2AuthenticationFailureHandler;
import net.bzk.auth.security.oauth.OAuth2AuthenticationSuccessHandler;
import net.bzk.auth.service.AccountService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@Autowired
	private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/authenticate", "/register", "/test").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// all other requests need to be authenticated
				.antMatchers("/admin/**").hasAuthority("Admin").antMatchers("/cosign/**").hasAuthority("EndUser")
				.antMatchers("/explore/**").hasAuthority("EndUser").anyRequest().authenticated().and()

				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
				 .oauth2Login()
                 .authorizationEndpoint()
                     .baseUri("/oauth2/authorize")
                     .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                     .and()
                 .redirectionEndpoint()
                     .baseUri("/oauth2/callback/*")
                     .and()
                 .userInfoEndpoint()
                     .userService(customOAuth2UserService)
                     .and()
                 .successHandler(oAuth2AuthenticationSuccessHandler)
                 .failureHandler(oAuth2AuthenticationFailureHandler)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}