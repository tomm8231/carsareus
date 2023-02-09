package dat3.car.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.car.entity.Car;
import lombok.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarRequest {
    private int id;
    private String brand;
    private String model;
    private double pricePrDay;
    private Integer bestDiscount;

    public static Car getCarEntity(CarRequest c) {
      Car car = new Car(c.brand,c.model,c.pricePrDay,c.bestDiscount);
      return car;
    }

    public CarRequest(Car c) {
        this.brand = c.getBrand();
        this.model = c.getModel();
        this.pricePrDay = c.getPricePrDay();
        this.bestDiscount = getBestDiscount();
    }


}
