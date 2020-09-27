package net.bzk.infrastructure.i18n;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ZhConvertService {
	

	public enum Type{
		TRADITIONAL ,SIMPLIFIED
	}
	
	@Value("classpath:zh2Hans.properties")
	private Resource zh2HansFile;
	@Value("classpath:zh2Hant.properties")
	private Resource zh2HantFile;
	
	private ZHConverter tConverter,sConverter;
	
	
	@PostConstruct
	void init() throws IOException {
		tConverter = new ZHConverter(zh2HantFile.getInputStream());
		sConverter = new ZHConverter(zh2HansFile.getInputStream());
	}
	
	public String convert(String str , Type t) {
		if(t== Type.SIMPLIFIED) return sConverter.convert(str);
		if(t== Type.TRADITIONAL) return tConverter.convert(str);
		throw new NullPointerException("not support t:"+t);
	}

}
