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
      Car c1 = new Car("Opel", "Vectra", 700.00, 100);
      Car c2 = new Car("Toyota", "Yaris", 500.00, 100);
      Car c3 = new Car("Opel", "Vectra", 700.00, 100);
      cars.add(c1);
      cars.add(c2);
      cars.add(c3);
      carRepository.saveAllAndFlush(cars);
      dataIsReady = true;
    }


  }

  @Test
  void findByBrandAndModel() {
    List<Car> carsFound = carRepository.findByBrandAndModel("Opel", "Vectra");
    assertEquals(2, carsFound.size());
  }
}