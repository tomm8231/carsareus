package dat3.car.repository;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

  boolean existsByCarAndRentalDate(Car car, LocalDate rentalDate);

  List<Reservation> findAllByMember(Member member);

}
