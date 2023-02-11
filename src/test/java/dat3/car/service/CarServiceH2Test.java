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


  @BeforeEach
  void setUp() {
    if (!dataIsReady) {  //Explain this
      carRepository.save(new Car("Opel", "Vectra", 500.00, 20));
      carRepository.save(new Car("Toyota", "Yaris", 400.00, 25));
      dataIsReady = true;
      carService = new CarService(carRepository); //Real DB is mocked away with H2
    }
  }


/*
Nedenstående dependency injection er foreslået af chatgpt - sammen med nedenstående @AfterEach-annotering + metode.
Alternativt fejler deleteCarById(), setBestDiscount() og findCarById(), når alle tests bliver kørt på een gang
- men består, når de bliver kørt individuelt

 */


  @Autowired
  private EntityManagerFactory entityManagerFactory;

  //Denne @AfterEach er foreslået af chatgpt, da id'et på car ikke blev genstartet i @BeforeEach
  @AfterEach
  void resetPrimaryKey() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.createNativeQuery("ALTER TABLE CAR ALTER COLUMN id RESTART WITH 1").executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Test
  void getCars() {
    List<CarResponse> cars = carService.getCars(true);
    assertEquals(2, cars.size());
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
    carService.editCar(body, car.getId());

    assertEquals(1000, car.getPricePrDay());
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


