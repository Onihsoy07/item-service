package hello.itemservice.web;

import hello.itemservice.config.SessionManager;
import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.argumentResolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

//    @GetMapping({"", "/"})
    public String indexLoginV2(HttpServletRequest request,
                               Model model) {

        Member sessionMember = (Member) sessionManager.getSession(request);

        if (sessionMember == null) {
            return "home";
        }

        model.addAttribute("member", sessionMember);
        return "loginHome";
    }

//    @GetMapping({"", "/"})
    public String indexLoginV3(HttpServletRequest request,
                               Model model) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @SessionAttribute는 세션을 찾을 때만 사용(생성 안됨)
//    @GetMapping({"", "/"})
    public String indexLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                     Model model) {

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping({"", "/"})
    public String indexLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
