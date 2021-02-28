package net.bzk.auth;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import net.bzk.auth.aop.CurrentUserMethodArgumentResolver;
import net.bzk.infrastructure.CommUtils;

//TODO this enable can overwrite @See(BzkFlowUtils.getFlowJsonMapper)

//@Configuration
//@EnableWebMvc
//public class FastMvcConfiguration implements WebMvcConfigurer {
//
//	@Inject
//	private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;
//
//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//		resolvers.add(currentUserMethodArgumentResolver);
//		CommUtils.pl("addArgumentResolvers DONE:" + currentUserMethodArgumentResolver);
//	}
//	
//
//
//}