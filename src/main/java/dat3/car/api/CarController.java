package dat3.car.api;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.service.CarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cars")
public class CarController {

  private CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  //Admin only
  @GetMapping
  List<CarResponse> getMembers(){
    return carService.getCars(true);
  }

  //Admin and User and Anonymous
  @GetMapping(path = "/{id}")
  CarResponse getCarById(@PathVariable int id) throws Exception {
    return carService.findCarById(id);
  }

  //Admin
  //@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping
  CarResponse addCar(@RequestBody CarRequest body) {
    return carService.addCar(body);
  }

  //Admin
  @PutMapping("/{id}")
  ResponseEntity<Boolean> editCar(@RequestBody CarRequest body, @PathVariable int id) {
    return carService.editCar(body, id);
  }

  //PatchMapping for bestDiscount
  @PatchMapping("/best-discount/{id}/{value}")
  void setDiscountForCar(@PathVariable int id, @PathVariable Integer bestDiscount) {
    carService.setBestDiscount(id, bestDiscount);
  }

  //DeleteMapping for deleting member by username
  @DeleteMapping("/{id}")
  void deleteCarById(@PathVariable int id) {
    carService.deleteCarById(id);
  }

}
