package net.bzk.auth;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.bzk.auth.aop.CurrentUserMethodArgumentResolver;
import net.bzk.infrastructure.CommUtils;

@Configuration
@EnableWebMvc
public class FastMvcConfiguration implements WebMvcConfigurer {

	@Inject
	private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentUserMethodArgumentResolver);
		CommUtils.pl("addArgumentResolvers DONE:" + currentUserMethodArgumentResolver);
	}

}