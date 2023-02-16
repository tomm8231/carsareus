package dat3.car.config;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import dat3.security.entity.Role;
import dat3.security.entity.UserWithRoles;
import dat3.security.repository.UserWithRolesRepository;
import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Configuration
//@EnableJpaRepositories(basePackages = {"dat3.security.repository", "dat3.car.repository"})
//@ComponentScan(basePackages = "dat3.security")
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

    setupUserWithRoleUsers();

  }

  @Autowired
  UserWithRolesRepository userWithRolesRepository;
  //final String passwordUsedByAll = "test12";

  /*****************************************************************************************
   NEVER  COMMIT/PUSH CODE WITH DEFAULT CREDENTIALS FOR REAL
   iT'S ONE OF THE TOP SECURITY FLAWS YOU CAN DO
   *****************************************************************************************/
  private void setupUserWithRoleUsers() {

    System.out.println("******************************************************************************");
    System.out.println("******* NEVER  COMMIT/PUSH CODE WITH DEFAULT CREDENTIALS FOR REAL ************");
    System.out.println("******* REMOVE THIS BEFORE DEPLOYMENT, AND SETUP DEFAULT USERS DIRECTLY  *****");
    System.out.println("**** ** ON YOUR REMOTE DATABASE                 ******************************");
    System.out.println("******************************************************************************");
    UserWithRoles user1 = new UserWithRoles("user1", passwordUsedByAll, "user1@a.dk");
    UserWithRoles user2 = new UserWithRoles("user2", passwordUsedByAll, "user2@a.dk");
    UserWithRoles user3 = new UserWithRoles("user3", passwordUsedByAll, "user3@a.dk");
    UserWithRoles user4 = new UserWithRoles("user4", passwordUsedByAll, "user4@a.dk");
    user1.addRole(Role.USER);
    user1.addRole(Role.ADMIN);
    user2.addRole(Role.USER);
    user3.addRole(Role.ADMIN);
    //No Role assigned to user4
    userWithRolesRepository.save(user1);
    userWithRolesRepository.save(user2);
    userWithRolesRepository.save(user3);
    userWithRolesRepository.save(user4);
  }



}

