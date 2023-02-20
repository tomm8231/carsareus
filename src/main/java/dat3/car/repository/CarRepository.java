package dat3.car.repository;

import dat3.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Integer> {
  List<Car> findByBrandAndModel(String brand, String model);

  @Query("SELECT AVG(c.pricePrDay) FROM Car c")
  Double findAveragePricePerDay();

  @Query("SELECT MAX(c.bestDiscount) FROM Car c")
  Integer findBestDiscount();

  List<Car> findCarsByBestDiscount(Integer bestDiscount);

  // Find all cars that have not been reserved
  @Query("SELECT c FROM Car c WHERE c.id NOT IN (SELECT DISTINCT r.car.id FROM Reservation r)")
  List<Car> getCarsWithoutReservation();

}
