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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarServiceH2Test {

  @Autowired
  public CarRepository carRepository;

  public CarService carService;


  boolean dataIsReady = false;

private Car car1;
private Car car2;
private Car car3;

  @BeforeEach
  void setUp() {
    if (!dataIsReady) {  //Explain this
      car1 = new Car("Opel", "Vectra", 500.00, 20);
      car2 = new Car("Toyota", "Yaris", 400.00, 25);
      car3 = new Car("Ford", "Fiesta", 300, 25);
      carRepository.saveAndFlush(car1);
      carRepository.saveAndFlush(car2);
      carRepository.saveAndFlush(car3);
      carService = new CarService(carRepository); //Real DB is mocked away with H2
      dataIsReady = true;

    }
  }


  @Test
  void getCars() {
    List<CarResponse> cars = carService.getCars(true);
    assertEquals(3, cars.size());
  }

  @Test
  void findCarById() {
    CarResponse response = carService.findCarById(car1.getCarId());
    assertEquals("Vectra", response.getModel());
  }

  @Test
  void addCar() {
    Car car = new Car("Tesla", "X", 900.00, 10);
    CarRequest carRequest = new CarRequest(car);
    carService.addCar(carRequest);

    List<Car> cars = carRepository.findAll();

    assertEquals(4,cars.size());


    //Hvorfor virker nedenstående ikke?
    /*
    Optional<Car> optionalCar = carRepository.findById(3); //# 3 fordi der burde være tre biler
    assertEquals("X", optionalCar.get().getModel());
     */
  }

  @Test
  void editCar() {

    Car car = new Car("Tesla", "Model S", 1000.00, 4);
    carRepository.save(car);

    CarRequest body = new CarRequest(car);
    body.setPricePrDay(1200);
    ResponseEntity<Boolean> response = carService.editCar(body,car.getCarId());

    assertTrue(response.hasBody());
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


  @Test
  void findCarsByBrandAndModel() {
    List<Car> cars = carRepository.findByBrandAndModel("Opel", "Vectra");
    assertEquals(1, cars.size());

  }


  @Test
  void findCarsByBestDiscount() {
    List<CarResponse> carsWithBestDiscount = carService.findCarsByBestDiscount();
    assertFalse(carsWithBestDiscount.isEmpty());
    assertEquals(2, carsWithBestDiscount.size());
    assertEquals(25, carsWithBestDiscount.get(0).getBestDiscount());
  }
}


