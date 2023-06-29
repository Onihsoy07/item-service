package hello.itemservice.domain.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;

    @NotBlank
    private String loginId;
    @NotBlank
    private String name;
    @NotBlank
    private String password;

}
