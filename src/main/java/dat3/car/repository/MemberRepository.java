package dat3.car.repository;

import dat3.car.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member,String> {

  boolean existsByEmail(String email);


  @Transactional
  @Modifying
  @Query("update Member m set m.ranking = ?1 where m.username = ?2")
  void updateRankingForUser(int value, String username);
}
