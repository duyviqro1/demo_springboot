package vn.iotstar.configs;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean(name = "localeResolver")
	public LocaleResolver getLocaleResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setCookieDomain("iotstar.vn");
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}
	@Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource(); 

        // Đọc các file properties từ thư mục src/main/resources/i18n
        messageSource.setBasename("classpath:i18n/messages");
        // Mã hóa mặc định UTF-8
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) 
 {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor(); 
        // Tham số để thay đổi ngôn ngữ
        localeChangeInterceptor.setParamName("language");
        // Áp dụng interceptor cho tất cả các request
        registry.addInterceptor(localeChangeInterceptor).addPathPatterns("/**");
    }
}
