package dat3.car.service;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.dto.ReservationRequest;
import dat3.car.dto.ReservationResponse;
import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

  private ReservationRepository reservationRepository;
  private MemberRepository memberRepository;
  private CarRepository carRepository;

  public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, CarRepository carRepository) {
    this.reservationRepository = reservationRepository;
    this.memberRepository = memberRepository;
    this.carRepository = carRepository;
  }

  public ReservationResponse makeReservation(ReservationRequest reservationRequest){


    Car car = carRepository.findById(reservationRequest.getCarId()).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.BAD_REQUEST, "car with this id does not exist"));

    if (reservationRequest.getRentalDate().isBefore(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date");
    }

    if (reservationRepository.existsByCarAndRentalDate(car, reservationRequest.getRentalDate())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reservation with the given date and car is already made");
    }

    Member member = memberRepository.findById(reservationRequest.getUsername()).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.BAD_REQUEST, "member with this id does not exist"));

    Reservation reservation = new Reservation(reservationRequest.getRentalDate(), member, car);

    reservationRepository.save(reservation);

    return new ReservationResponse(reservation);


  }

  public List<ReservationResponse> findAllReservationsByMember(String username) {
    Member member = memberRepository.findById(username).orElseThrow(
        () ->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member does not exist"));
    List<Reservation> reservations = reservationRepository.findAllByMember(member);
    List<ReservationResponse> responses = reservations.stream().map(r -> new ReservationResponse(r)).toList();

    return responses;
  }



  public List<ReservationResponse> getReservations() {
    List<Reservation> reservations = reservationRepository.findAll();
    return reservations.stream().map(r -> new ReservationResponse(r)).toList();
  }
}
