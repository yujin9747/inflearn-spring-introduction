package yujin.yujinspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import yujin.yujinspring.domain.Member;
import yujin.yujinspring.service.MemberService;

import java.util.List;

@Controller
public class MemberController {

    // instance를 새로 생성할 필요 없이, Spring container에 한 번 등록해 놓고 공용으로 쓰면 됨.
    private final MemberService memberService;

    // Autowired 를 하면 container에서 자동으로 가져와서 연결해줌 -> 이걸 해주기 위해서는 MemberService에 @Service 붙여주기
    // dependency injection
    // @를 통해서 한 방식은 "스프링 빈을 등록하는 2가지 방법" 중 1번째 방식 "컴포넌트 스캔과 자동 의존관계 설정"
    // 방법 2 : 자바 코드로 직접 스프링 빈 등록하기 -> https://www.inflearn.com/course/스프링-입문-스프링부트/unit/49587?tab=curriculum 영상 참고
    // SpringConfig 파일 참고
    // @Service, @Repository 모두 @Component의 특수화된 케이스
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    // form으로 submit을 하면 매핑되는 /members/new는 PostMapping!!! -> form의 method를 post로 설정해 놨기 때문.
    // post 매핑은 보통 데이터를 넣어서 전달할 때 사용, 조회할 땐 get을 사용
    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
