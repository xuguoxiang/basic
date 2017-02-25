package com.xiang.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by Administrator on 2017/2/25.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = {"com.xiang"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class, RestController.class, ControllerAdvice.class})})
//@ImportResource() //导入spring.xml配置文件
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * 配置视图解析器
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/pages/");
        resolver.setSuffix(".jsp");
        //可以在JSP页面中通过${}访问beans
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;

    }

    /**
     * 配置文件上传配置
     *
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20000000);
        return multipartResolver;
    }

    /**
     * 通过调用configer.enable()，DispatcherServlet将会把针对静态资源的请求
     * 转交给servlert容器的default servlet处理。
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

        configurer.enable();
    }

    /**
     * 设置校验器
     *
     * @return
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.setValidationMessageSource(messageSource());
        return validatorFactoryBean;
    }

    /**
     * 配置资源文件
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:resource/ValidationMessages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    /**
     * 国际化配置
     *
     * @return
     */
    @Bean
    public CookieLocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
}
