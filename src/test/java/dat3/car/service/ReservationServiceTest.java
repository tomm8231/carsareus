package dat3.car.service;

import dat3.car.dto.MemberResponse;
import dat3.car.dto.ReservationRequest;
import dat3.car.dto.ReservationResponse;
import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("dat3.car.service")
/*
@ComponentScan("dat3.car.service") is an annotation in Spring Framework that tells Spring where to look for
Spring-managed components, such as Spring beans, within a package or packages. In this case, it specifies that Spring
should scan the dat3.car.service package and its sub-packages to find Spring-managed components.
 */
class ReservationServiceTest {

  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  CarRepository carRepository;

  ReservationService reservationService;




  boolean dataIsReady = false;

  private Car car1;
  private Car car2;
  private Member member1;
  private Member member2;
  private LocalDate rentalDate1;
  private LocalDate rentalDate2;

  @BeforeEach
  void setUp() {
    if (!dataIsReady) {  //Explain this
      car1 = new Car("Opel", "Vectra", 500.00, 20);
      car2 = new Car("Toyota", "Yaris", 400.00, 25);
      carRepository.saveAndFlush(car1);
      carRepository.saveAndFlush(car2);

      member1 = new Member("m1", "test12", "m1@a.dk", "bb",
          "Olsen", "xx vej 34", "Lyngby", "2800");
      member2 = new Member("m2", "test12", "m2@a.dk", "aa",
          "hansen", "xx vej 34", "Lyngby", "2800");
      memberRepository.saveAndFlush(member1);
      memberRepository.saveAndFlush(member2);

      rentalDate1 = LocalDate.parse("2023-08-08");
      rentalDate2 = LocalDate.parse("2023-07-07");


     reservationService = new ReservationService(reservationRepository,
         memberRepository, carRepository); //Real DB is mocked away with H2

      dataIsReady = true;

    }
  }

  @Test
  void makeReservation() {
    ReservationRequest reservationRequest = new ReservationRequest();
    reservationRequest.setCarId(car1.getCarId());
    reservationRequest.setUsername(member1.getUsername());
    reservationRequest.setRentalDate(rentalDate1);

    ReservationResponse reservationResponse = reservationService.makeReservation(reservationRequest);
    assertEquals(LocalDate.parse("2023-08-08"), reservationResponse.getRentalDate());
  }

  @Test
  void countReservationsByMember() {
    Reservation reservation1 = new Reservation(rentalDate1, member1, car1);
    Reservation reservation2 = new Reservation(rentalDate2, member1, car2);
    reservationRepository.saveAndFlush(reservation1);
    reservationRepository.saveAndFlush(reservation2);

    assertEquals(2,reservationService.countReservationsByMember(member1));
    assertEquals(0,reservationService.countReservationsByMember(member2));
  }
}