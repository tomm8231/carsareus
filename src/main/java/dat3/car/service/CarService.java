package dat3.car.service;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarService {

  private CarRepository carRepository;

  public CarService(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  public List<CarResponse> getCars(boolean includeAll) {
    List<Car> cars = carRepository.findAll();
    List<CarResponse> carResponses = cars.stream().map(c -> new CarResponse(c, includeAll)).toList();
    return carResponses;
  }


  public CarResponse findCarById(int id) {
    Car car = carRepository.findById(id).orElseThrow(()->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with this ID does not exist"));
    return new CarResponse(car, true);
  }


  public CarResponse addCar(CarRequest carRequest) {
    if(carRepository.existsById(carRequest.getId())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Car with this ID already exist");
    }

    Car newCar = CarRequest.getCarEntity(carRequest);
    carRepository.save(newCar);
    return new CarResponse(newCar, false);
  }

  public ResponseEntity<Boolean> editCar(CarRequest body, int id) {

   Car car = carRepository.findById(id).orElseThrow(()->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with this ID does not exist"));

    car.setBrand(body.getBrand());
    car.setModel(body.getModel());
    car.setPricePrDay(body.getPricePrDay());
    car.setBestDiscount(body.getBestDiscount());
    carRepository.save(car);

    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  public void setBestDiscount(int id, Integer bestDiscount) {
    Car car = carRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Car with this ID does not exist"));
    car.setBestDiscount(bestDiscount);
    carRepository.save(car);
  }

  public void deleteCarById(int id) {
    Car car = carRepository.findById(id).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"Car with this ID does not exist"));
    carRepository.delete(car);
  }
}
