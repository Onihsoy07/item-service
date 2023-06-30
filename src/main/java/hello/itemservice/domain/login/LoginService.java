package hello.itemservice.domain.login;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return f
     */
    public Member login(String loginId, String password) {
//        Optional<Member> findMember = memberRepository.findByLonginId(loginId);
//        Member member = findMember.get();
//        if (member.getPassword().equals(password)) {
//            return member;
//        } else {
//            return null;
//        }

        //위랑 같은거
        return memberRepository.findByLonginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
