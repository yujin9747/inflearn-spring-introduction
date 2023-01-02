package yujin.yujinspring.domain;

import javax.persistence.*;

/**
 * jpa를 사용하기 위해서 @Entity annotation 추가
 * -> jpa가 관리하도록 명시하는 것.
 * jpa는 Object Relational Mapping(OPA)방식.
 * Relational -> 관계형 데이타
 * **/
@Entity
public class Member {

    // jpa 관련 annotation : @Id, @GeneratedValue
    // GenerationType.IDENTITY -> 값을 생성할 때 Id 키 값을 DB에서 자동으로 increment해서 넣어주는 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "userName") -> member table 내의 userName 이라는 column명과 name을 매핑하고 싶을 때 사용.
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
