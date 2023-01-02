package yujin.yujinspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yujin.yujinspring.aop.TimeTraceAop;
import yujin.yujinspring.repository.*;
import yujin.yujinspring.service.MemberService;

// 자바코드로 직접 컴포넌트를 등록하기 위한 코드
@Configuration
public class SpringConfig {

    // Controller는 Controller 파일 내에서 직접 annotation을 넣어주기

    // Spring으로 부터 DataSource를 받아와서 JdbcMemberRepository에 주입해주기 -> jpa 사용할 때는 entity manager를 받아와야 하므로 주석처리
//    private DataSource dataSource;
//
//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    // Spring data jpa를 사용할 때는 필요 없는 코드
//    private EntityManager em;
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }

    // Spring data jpa를 사용할 때의 코드. -> interface에서 extends한 MemberRepository를 받아와서 injection 해주면 사용할 수 있음
    private final MemberRepository memberRepository;

    // @Autowired -> 생성자 하나인 경우 생략 가능
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


//    @Bean
//    public MemberService memberService(){
//
//        return new MemberService(memberRepository());
//    }

    // Spring data jpa 사용할 때 다음과 같이 MemberService에 injection 해줌
    @Bean
    public MemberService memberService(){

        return new MemberService(memberRepository);
    }

    // 강의에서는 여기서 bean으로 등록해도 된다고 했지만, Circular Dependencies 에러가 떠서 @Component로 스캔함
//    @Bean
//    public TimeTraceAop timeTraceAop() {
//        return new TimeTraceAop();
//    }

    // Spring data jpa를 사용할 때는 필요 없는 코드
//    @Bean
//    public MemberRepository memberRepository() {
//
//        // return new MemoryMemberRepository();
//        // return new JdbcMemberRepository(dataSource);
//        // 메모리 기반으로 개발했다가 db를 갈아끼우는 과정!!
//        // configuration 파일로 직접 bean을 등록하는 것의 장점
//        //return new JdbcTemplateMemberRepository(dataSource);
//        //return new JpaMemberRepository(em);
//    }

}
