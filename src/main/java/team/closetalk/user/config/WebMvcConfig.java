package team.closetalk.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.closetalk.community.enumeration.StringToCategoryConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {



    // 커뮤니티 카테고리 enum String으로 전환에 필요
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToCategoryConverter());
    }
}