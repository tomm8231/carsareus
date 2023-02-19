package dat3.car.repository;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ReservationRepositoryTest {


  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  CarRepository carRepository;

  ReservationService reservationService;


  boolean dataIsReady = false;

  private Reservation reservation1;
  private Reservation reservation2;
  private Reservation reservation3;
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


      reservation1 = new Reservation(rentalDate1,member1,car1);
      reservation2 = new Reservation(rentalDate2,member2,car2);
      reservation3 = new Reservation(rentalDate2, member2, car1);

      reservationRepository.saveAndFlush(reservation1);
      reservationRepository.saveAndFlush(reservation2);
      reservationRepository.saveAndFlush(reservation3);



      reservationService = new ReservationService(reservationRepository,
          memberRepository, carRepository); //Real DB is mocked away with H2

      dataIsReady = true;

    }
  }


  @Test
  void findAllReservationsByMember() {
    List<Reservation> responsesMember1 = reservationRepository.findAllByMember(member1);
    List<Reservation> responsesMember2 = reservationRepository.findAllByMember(member2);
    assertEquals(1, responsesMember1.size());
    assertEquals(2,responsesMember2.size());

  }

  @Test
  void countReservationsByMember() {
    assertEquals(2,reservationRepository.countReservationsByMember(member2));
    assertEquals(1, reservationRepository.countReservationsByMember(member1));
  }
/*
  @Test
  void existsByCarAndRentalDate() {
    assertTrue(reservationRepository.existsByCarAndRentalDate(car1, rentalDate1));
  }

 */
}