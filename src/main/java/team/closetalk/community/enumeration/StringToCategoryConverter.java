package team.closetalk.community.enumeration;

import org.springframework.core.convert.converter.Converter;
import team.closetalk.community.enumeration.Category;


public class StringToCategoryConverter implements Converter<String, Category> {

    // 카테고리 컨버터
    @Override
    public Category convert(String source) {
        try {
            return Category.valueOf(source.toUpperCase()); // 대소문자 구분 없이 변환
        } catch (IllegalArgumentException e) {
            // 열거형에 매칭되지 않는 경우 예외 처리
            throw new IllegalArgumentException("Invalid category: " + source);
        }
    }
}
