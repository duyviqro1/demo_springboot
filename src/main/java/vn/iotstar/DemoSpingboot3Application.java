package vn.iotstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import vn.iotstar.configs.MySiteMeshFilter;

@SpringBootApplication
public class DemoSpingboot3Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpingboot3Application.class, args);
	}

	@Bean
	public FilterRegistrationBean<MySiteMeshFilter> siteMeshFilter() {
	    FilterRegistrationBean<MySiteMeshFilter> filterRegistrationBean = new FilterRegistrationBean<>();
	    filterRegistrationBean.setFilter(new MySiteMeshFilter()); 
	    filterRegistrationBean.addUrlPatterns("/*");
	    return filterRegistrationBean;
	}

}
