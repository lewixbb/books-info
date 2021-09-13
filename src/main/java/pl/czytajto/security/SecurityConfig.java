package pl.czytajto.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.LazyCsrfTokenRepository;

import pl.czytajto.security.filters.UserEnableFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder ;
	}
		
	@Override
	 protected void configure(HttpSecurity http) throws Exception {
	     http
	     .httpBasic().and()
	     .csrf()
	         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
	     .authorizeRequests()
	     	 .antMatchers("/registration").permitAll()
	         .antMatchers(HttpMethod.POST).authenticated().and()
	     .addFilterBefore(getBefAuthenticationFilter(), UserEnableFilter.class)    
	     .sessionManagement()
	         .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	 }
	
	public UsernamePasswordAuthenticationFilter getBefAuthenticationFilter() throws Exception {
		
		UserEnableFilter filter = new UserEnableFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				
				System.out.println("test filtra w configu");			
				super.onAuthenticationFailure(request, response, exception);
				
				response.sendRedirect("/");;
			}				
		});	
		return filter;
		
	}
			
}
