package yujin.yujinspring.repository;

import org.springframework.jdbc.datasource.DataSourceUtils;
import yujin.yujinspring.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 15~20년 전에 쓰던 방식.
 * 순수 JDBC로 코딩하는 방식을 보여줌.
 * 코드가 많이 복잡하기 때문에 강의 자료에서 복사 붙여 넣기 해서 설명해 주심.
 * **/
public class JdbcMemberRepository implements MemberRepository {

    private final DataSource datasource;

    // Spring이 만들어 놓은 datasource를 주입 받아야 함.
    public JdbcMemberRepository(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Member save(Member member) {
        // 쿼리문 생성 -> 쿼리문은 메소드 밖에 상수 값으로 해두는 것이 더 좋음
        String sql = "insert into member(name) values(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // db 연결
            conn = getConnection();
            // ? 값을 세팅하기 위한 prepareStatement 객체를 가져옴
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            // ? 값에 이름 세팅
            pstmt.setString(1, member.getName());
            // db에 쿼리문 날라감
            pstmt.executeUpdate();
            // 쿼리문 날린 결과에서 생성된 키 값을 가져옴 -> primary key를 id로 설정해 둠
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                // 자동 생성된 id값을 member에 세팅
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            // 연결 끊기
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            // 조회를 할 때는 executeQuery()를 사용
            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    private Connection getConnection() {
        // database connection을 여러개 생성하지 않고 같은 connection을 유지해주기 위해 DataSourceUtils로 getConnection
        return DataSourceUtils.getConnection(datasource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        // DataSourceUtils를 통해서 release
        DataSourceUtils.releaseConnection(conn, datasource);
    }
}
