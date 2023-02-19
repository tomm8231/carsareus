package dat3.car.repository;

import dat3.car.entity.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarRepositoryTest {

  @Autowired
  CarRepository carRepository;

  boolean dataIsReady = false;
  List<Car> cars = new ArrayList<>();


  @BeforeEach
  void setUp() {
    if (!dataIsReady) {
      Car c1 = new Car("Opel", "Vectra", 300.00, 10);
      Car c2 = new Car("Toyota", "Yaris", 200.00, 15);
      Car c3 = new Car("Opel", "Vectra", 100.00, 7);
      Car c4 = new Car("Ford", "Fiesta", 200.00, 15);
      cars.add(c1);
      cars.add(c2);
      cars.add(c3);
      cars.add(c4);
      carRepository.saveAllAndFlush(cars);
      dataIsReady = true;
    }


  }

  @Test
  void findByBrandAndModel() {
    List<Car> carsFound = carRepository.findByBrandAndModel("Opel", "Vectra");
    assertEquals(2, carsFound.size());
  }

  @Test
  void findAveragePricePerDay() {
    Double pricePrDay = carRepository.findAveragePricePerDay();
    assertEquals(200,pricePrDay);
  }

  @Test
  void findBestDiscount() {
    Integer bestDiscount = carRepository.findBestDiscount();
    assertEquals(15, bestDiscount);
  }

  @Test
  void findCarsByBestDiscount() {
    Integer bestDiscount = 15;
    List<Car> carsWithBestDiscount = carRepository.findCarsByBestDiscount(bestDiscount);

    assertEquals(2, carsWithBestDiscount.size());
    assertEquals("Toyota", carsWithBestDiscount.get(0).getBrand());
    assertEquals("Ford", carsWithBestDiscount.get(1).getBrand());
  }
}