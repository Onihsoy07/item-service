package hello.itemservice.web.basic;

import hello.itemservice.domain.item.SaveItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/basic")
public class BasicItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated SaveItemDto saveItemDto,
                          BindingResult bindingResult) {
        log.info("API addItem Controller 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors = {}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공로직");
        return saveItemDto;
    }
}
