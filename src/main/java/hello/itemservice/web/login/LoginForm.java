package hello.itemservice.web.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
