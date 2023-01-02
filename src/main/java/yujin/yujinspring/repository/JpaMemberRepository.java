package yujin.yujinspring.repository;

import yujin.yujinspring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    // jpa는 EntityManager로 모든 것이 동작함.
    private final EntityManager em;

    // Spring이 생성한 EntityManager를 injection 받기
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        // persist : 영구 저장
        em.persist(member);
        // persist가 따로 return 값이 없기 때문에 spec에 맞추기 위해 리턴 따로 작성해 준 것
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // 1st : 조회할 타입, 2nd : 식별자
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        // jpql 이라는 쿼리 언어를 사용해야 하는 경우
        // table을 대상으로 쿼리를 날리는 것이 아니라, 객체를 대상으로 쿼리를 날 -> sql로 번역 됨.
        // member entity 자체를 조회
        // 기존의 sql은 id, name값 찾은 후에 member 객체로 매핑해서 반환해야 했음.
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return result;
    }
}
