package dat3.car.service;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarServiceH2Test {

  @Autowired
  public CarRepository carRepository;

  CarService carService;

  boolean dataIsReady = false;

  @BeforeEach
  void setUp() {
    if (!dataIsReady) {  //Explain this
      carRepository.save(new Car("Opel","Vectra",500.00,20));
      carRepository.save(new Car("Toyota","Yaris",400.00,25));
      dataIsReady = true;
      carService = new CarService(carRepository); //Real DB is mocked away with H2
    }
  }

  @Test
  void getCars() {
    List<CarResponse> cars = carService.getCars(true);
    assertEquals(2,cars.size());
  }

  @Test
  void findCarById() {
    Car car = carRepository.findById(1).get();
    assertEquals("Vectra", car.getModel());
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
    carService.editCar(body,car.getId());

    assertEquals(1000,car.getPricePrDay());
  }

  @Test
  void setBestDiscount() {
    Car car = carRepository.findById(2).get();
    carService.setBestDiscount(car.getId(), 5);
    assertEquals(5, car.getBestDiscount());
  }

  @Test
  void deleteCarById() {
    Car car = carRepository.findById(1).get();
    carService.deleteCarById(car.getId());
    assertFalse(carRepository.existsById(car.getId()));
  }
}