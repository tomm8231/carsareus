package dat3.car.service;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarServiceH2Test {

  @Autowired
  public CarRepository carRepository;

  public CarService carService;


  boolean dataIsReady = false;

private Car car1;
private Car car2;

  @BeforeEach
  void setUp() {
    if (!dataIsReady) {  //Explain this
      car1 = new Car("Opel", "Vectra", 500.00, 20);
      car2= new Car("Toyota", "Yaris", 400.00, 25);
      carRepository.saveAndFlush(car1);
      carRepository.saveAndFlush(car2);
      carService = new CarService(carRepository); //Real DB is mocked away with H2
      dataIsReady = true;

    }
  }


  @Test
  void getCars() {
    List<CarResponse> cars = carService.getCars(true);
    assertEquals(2, cars.size());
  }

  @Test
  void findCarById() {
    CarResponse response = carService.findCarById(car1.getCarId());
    assertEquals("Vectra", car1.getModel());
  }

  @Test
  void addCar() {
    Car car = new Car("Tesla", "X", 900.00, 10);
    CarRequest carRequest = new CarRequest(car);
    carService.addCar(carRequest);
    assertNotNull(carRepository.findById(3).get());
  }

  @Test
  void editCar() {
    Car car = carRepository.findById(1).get();
    CarRequest body = new CarRequest(car);
    body.setPricePrDay(1000);
    carService.editCar(body, car.getCarId());

    assertEquals(1000, car.getPricePrDay());
  }

  @Test
  void setBestDiscount() {
    carService.setBestDiscount(car1.getCarId(), 5);
    assertEquals(5, car1.getBestDiscount());
  }

  @Test
  void deleteCarById() {
    carService.deleteCarById(car1.getCarId());
    assertFalse(carRepository.existsById(car1.getCarId()));
  }




}


