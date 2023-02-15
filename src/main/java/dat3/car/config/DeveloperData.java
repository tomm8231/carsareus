package dat3.car.config;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import org.hibernate.mapping.Map;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Configuration
public class DeveloperData implements ApplicationRunner {

  MemberRepository memberRepository;
  CarRepository carRepository;
  ReservationRepository reservationRepository;

  public DeveloperData(MemberRepository memberRepository, CarRepository carRepository, ReservationRepository reservationRepository) {
    this.memberRepository = memberRepository;
    this.carRepository = carRepository;
    this.reservationRepository = reservationRepository;
  }

  private final String passwordUsedByAll = "test12";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Member m1 = new Member("member1", passwordUsedByAll, "memb1@a.dk", "Kurt", "Wonnegut", "Lyngbyvej 2", "Lyngby", "2800");
    Member m2 = new Member("member2", passwordUsedByAll, "aaa@dd.dk", "Hanne", "Wonnegut", "Lyngbyvej 2", "Lyngby", "2800");


    memberRepository.save(m1);
    memberRepository.save(m2);
    Car c1 = new Car("Opel", "Vectra", 700.00, 100);
    Car c2 = new Car("Toyota", "Yaris", 300.00, 50);

    //Car.builder().brand("Suzuki").model("Swift").pricePrDay(500).bestDiscount(10).build();

    carRepository.save(c1);
    carRepository.save(c2);

    LocalDate rentalDate1 = LocalDate.parse("2023-05-05");
    Reservation reservation1 = new Reservation(rentalDate1, m1, c1);

    LocalDate rentalDate2 = LocalDate.parse("2023-06-06");
    Reservation reservation2 = new Reservation( rentalDate2, m2, c2);

    reservationRepository.save(reservation1);
    reservationRepository.save(reservation2);


  }


}

