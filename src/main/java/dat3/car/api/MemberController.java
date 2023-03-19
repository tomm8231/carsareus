package dat3.car.api;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;
import dat3.car.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
@CrossOrigin
class MemberController {

  private MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping
  List<MemberResponse> getMembers(){
    return memberService.getMembers(true);
  }

  //Admin
  @GetMapping(path = "/{username}")
  MemberResponse getMemberById(@PathVariable String username) throws Exception {
    return memberService.findMemberByUsername(username);
  }

  //Anonymous
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  MemberResponse addMember(@RequestBody MemberRequest body){
    return memberService.addMember(body);
  }

  //Member
  @PutMapping("/{username}")
  ResponseEntity<Boolean> editMember(@RequestBody MemberRequest body, @PathVariable String username){
    return memberService.editMember(body, username);
  }

  //Admin
  @PatchMapping("/ranking/{username}/{value}")
  void setRankingForUser(@PathVariable String username, @PathVariable int value) {
    memberService.setRankingForUser(username, value);
  }

  //Admin
  @DeleteMapping("/{username}")
  void deleteMemberByUsername(@PathVariable String username) {
    memberService.deleteMemberByUsername(username);
  }

@GetMapping("/with-reservation")
  List<MemberResponse> findMembersWithReservations() {
    return memberService.findMembersWithReservations();
}


}



