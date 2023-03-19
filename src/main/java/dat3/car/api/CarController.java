package dat3.car.api;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.service.CarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cars")
@CrossOrigin
public class CarController {

  private CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  //Admin only
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
  @GetMapping()
  List<CarResponse> getCars(){
    return carService.getCars(true);
  }



  //Admin and User and Anonymous
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
  @GetMapping(path = "/{id}")
  CarResponse getCarById(@PathVariable int id) throws Exception {
    return carService.findCarById(id);
  }

  //Admin
  //@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  CarResponse addCar(@RequestBody CarRequest body) {
    return carService.addCar(body);
  }

  //Admin
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/{id}")
  ResponseEntity<Boolean> editCar(@RequestBody CarRequest body, @PathVariable int id) {
    return carService.editCar(body, id);
  }

  //PatchMapping for bestDiscount
  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/best-discount/{id}/{value}")
  void setDiscountForCar(@PathVariable int id, @PathVariable Integer bestDiscount) {
    carService.setBestDiscount(id, bestDiscount);
  }

  //DeleteMapping for deleting member by username
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/{id}")
  void deleteCarById(@PathVariable int id) {
    carService.deleteCarById(id);
    //Se Lars' bud på github
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
  @GetMapping("/brand-model/{brand}/{model}")
  List<CarResponse> getCarsByBrandAndModel(@PathVariable String brand, @PathVariable String model) {
    return carService.findCarsByBrandAndModel(brand, model);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/average-price-per-day")
  Double findAveragePricePrDay() {
    return carService.findAveragePricePrDay();
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/best-discount")
  List<CarResponse> findCarsWithBestDiscount() {
    return carService.findCarsByBestDiscount();
  }



}
