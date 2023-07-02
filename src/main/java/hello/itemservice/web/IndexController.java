package hello.itemservice.web;

import hello.itemservice.config.SessionManager;
import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping({"", "/"})
    public String index() {
        return "home";
    }

//    @GetMapping({"", "/"})
    public String indexLogin(@CookieValue(name = "memberId", required = false) Long memberId,
                             Model model) {
        if (memberId == null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping({"", "/"})
    public String indexLoginV2(HttpServletRequest request,
                               Model model) {

        Member sessionMember = (Member) sessionManager.getSession(request);

        if (sessionMember == null) {
            return "home";
        }

        model.addAttribute("member", sessionMember);
        return "loginHome";
    }
}
