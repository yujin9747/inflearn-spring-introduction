package yujin.yujinspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yujin.yujinspring.repository.MemberRepository;
import yujin.yujinspring.repository.MemoryMemberRepository;
import yujin.yujinspring.service.MemberService;

// 자바코드로 직접 컴포넌트를 등록하기 위한 코드
@Configuration
public class SpringConfig {

    // Controller는 Controller 파일 내에서 직접 annotation을 넣어주기

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

}
