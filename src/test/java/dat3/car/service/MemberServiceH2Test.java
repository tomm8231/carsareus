package dat3.car.service;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Transactional (rollback)
//Includes what is needed for JPA/Hibernate - leaves everything else out
//Uses an in-memory database (H2)
@DataJpaTest
class MemberServiceH2Test {

  @Autowired
  public MemberRepository memberRepository;

  MemberService memberService;

  boolean dataIsReady = false;

  @BeforeEach
  void setUp() {
    if (!dataIsReady) {  //Explain this
      memberRepository.save(new Member("m1", "test12", "m1@a.dk", "bb",
          "Olsen", "xx vej 34", "Lyngby", "2800"));
      memberRepository.save(new Member("m2", "test12", "m2@a.dk", "aa",
          "hansen", "xx vej 34", "Lyngby", "2800"));
      dataIsReady = true;
      memberService = new MemberService(memberRepository); //Real DB is mocked away with H2
    }
  }


  @Test
  void addMember() {
    Member member = new Member("m3", "test12", "m3@a.dk", "aa",
        "hansen", "xx vej 34", "Lyngby", "2800");
    MemberRequest memberRequest = new MemberRequest(member);
    memberService.addMember(memberRequest);
    assertEquals("hansen", memberRequest.getLastName());
  }

  @Test
  void getMembersAdmin() {
    List<MemberResponse> members = memberService.getMembers(true);
    assertEquals(2, members.size());
    assertNotNull(members.get(0).getCreated());
  }

  @Test
  void getMemberById() {
    MemberResponse mr = memberService.findMemberByUsername("m1");
    assertEquals("m1@a.dk", mr.getEmail());
  }

  @Test
  void editMember() {
    Member member = memberRepository.findById("m1").get();
    MemberRequest body = new MemberRequest(member);
    body.setLastName("Jensen");
    memberService.editMember(body, member.getUsername());
    assertEquals("Jensen", member.getLastName());
  }

  @Test
  void setRankingForUser() {
    Member member = memberRepository.findById("m1").get();
    memberService.setRankingForUser(member.getUsername(), 50);

    assertEquals(50, member.getRanking());
  }

  @Test
  void deleteMemberByUsername() {
    Member member = memberRepository.findById("m1").get();
    memberService.deleteMemberByUsername(member.getUsername());
    assertFalse(memberRepository.existsById(member.getUsername()));
  }


}