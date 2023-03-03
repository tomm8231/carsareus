package dat3.car.api;

import dat3.car.dto.MemberResponse;
import dat3.car.dto.ReservationRequest;
import dat3.car.dto.ReservationResponse;
import dat3.car.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservations")
@CrossOrigin
public class ReservationController {

  private ReservationService reservationService;

  public ReservationController (ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  ReservationResponse makeReservation(@RequestBody ReservationRequest body) {
    return reservationService.makeReservation(body);
  }

  @GetMapping
  List<ReservationResponse> getReservations() {
    return reservationService.getReservations();
  }

  @GetMapping("/{username}")
  List<ReservationResponse> getAllReservationsByMember(@PathVariable String username) {
    return reservationService.findAllReservationsByMember(username);
  }

  @GetMapping("/{username}/count")
  Integer countReservationsByMember(@PathVariable String username) {
    return reservationService.countReservationsByMember(username);
  }
  
}
