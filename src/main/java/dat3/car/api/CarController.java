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
    return carService.getCars(false);
  }

  //Admin
  @GetMapping(path = "/{id}")
  CarResponse getCarById(@PathVariable int id) throws Exception {
    return carService.findCarById(id);
  }

  //Anonymous
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  CarResponse addCar(@RequestBody CarRequest body) {
    return carService.addCar(body);
  }

  //Member
  @PutMapping("/{id}")
  ResponseEntity<Boolean> editCar(@RequestBody CarRequest body, @PathVariable int id) {
    return carService.editCar(body, id);
  }


}
