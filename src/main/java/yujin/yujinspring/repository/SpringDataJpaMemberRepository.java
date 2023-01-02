package yujin.yujinspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yujin.yujinspring.domain.Member;

import java.util.Optional;

/**
 * jpa를 spring이 한 번 감싸 더 편리하게 사용할 수 있도록 하는 기술 : Spring data jpa
 * 주의!! jpa를 먼저 익힌 후, Spring data jpa를 사용할 것.
 * -> 따라서 이번 강의 내용은 경험 정도로 끝내고, jpa를 이용해서 "붕어빵 헌터" 프로젝트에 적용해 볼 예정
 * JpaRepository interface를 extends 하면 Spring에서 자동으로 구현체를 빈으로 등록을 해 주기 때문에 사용하기만 하면 됨.
 * 인터페이스의 메소드 이름만으로도 구현을 끝낼 수 있음!
 * -> 조합으로 만들기 어려운 기능은 Jpa가 제공하는 네이티브 쿼리를 사용하거나, JpaTemplate를 조합해서 사용하면 됨
 * **/

// jpaRepository 인터페이스를 extends할 때 1st : 객체 타입, 2st : Entity manager에서 key로 사용하는 것의 타입
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    // interface가 제공하는 공통화된 메소드말고 custom을 해야 할 때 아래와 같이 사용하면 됨.
    // 아래와 같이 custom을 생성하면 select m from Member m where m.name = ? 와 같은 jpql문을 알아서 생성해줌. 따로 구현할 필요 X
    // 메소드 이름 규칙만 따라주면 됨.
    @Override
    Optional<Member> findByName(String name);

    //예시 2
//    @Override
//    Optional<Member> findByNameAndId(String name, Long id);

    //예시 3
//    @Override
//    Optional<Member> findByNameOrId(String name, Long id);
}
