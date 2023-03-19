package dat3.car.service;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.dto.ReservationResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;
import dat3.security.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

  private MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }


  public MemberResponse addMember(MemberRequest memberRequest){

    if(memberRepository.existsById(memberRequest.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this ID already exist");
    }
    if(memberRepository.existsByEmail(memberRequest.getEmail())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this Email already exist");
    }


    Member newMember = MemberRequest.getMemberEntity(memberRequest);
    newMember.addRole(Role.USER);
    newMember = memberRepository.save(newMember);


    return new MemberResponse(newMember, false);
  }



  public List<MemberResponse> getMembers(boolean includeAll) {
    List<Member> members = memberRepository.findAll();
    //I stedet for at lave et for each loop hvor man overfører elementer fra listen til en ny liste:
    List<MemberResponse> memberResponses = members.stream().map(m->new MemberResponse(m,includeAll)).toList();
    return memberResponses;
  }

  public MemberResponse findMemberByUsername(String username) {
    Member member = memberRepository.findById(username).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Member with this ID does not exist"));

    //boolean hasReservation = memberRepository.existsMemberByReservationsIsNotEmpty(member);

    return new MemberResponse(member, true);
  }

  public ResponseEntity<Boolean> editMember(MemberRequest body, String username) {
    //Bør nok laves smartere, så member ikke kun er instancieret
    Member member = memberRepository.findById(username).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Member with this ID does not exist"));

    member.setFirstName(body.getFirstName());
    member.setLastName(body.getLastName());
    member.setEmail(body.getEmail());
    member.setStreet(body.getStreet());
    member.setZip(body.getZip());

    memberRepository.save(member);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }


  public void setRankingForUser(String username, int value) {
    Member member = memberRepository.findById(username).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Member with this ID does not exist"));

    member.setRanking(value);
    memberRepository.save(member);
  }

  public void deleteMemberByUsername(String username) {
    memberRepository.findById(username).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Member with this ID does not exist"));

    memberRepository.deleteById(username);
  }

  public List<MemberResponse> findMembersWithReservations() {
    List<Member> membersWithReservations = memberRepository.findMembersWithReservations();
    List<MemberResponse> responses = membersWithReservations.stream().map(m->new MemberResponse(m,true)).toList();
    return responses;
  }
}
