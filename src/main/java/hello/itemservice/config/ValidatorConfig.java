package hello.itemservice.config;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ValidatorConfig implements WebMvcConfigurer {

    //모든 MVC 컨트롤러의 @Validator에서 검증 실행
//    @Override
//    public Validator getValidator() {
//        return new ItemValidator();
//    }
}
