package dat3.car.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.car.entity.Car;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CarResponse {
  int id; //primary key
  String brand;
  String model;
  Double pricePrDay;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime createDateTime;
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime updateDateTime;

  Integer bestDiscount;

  public CarResponse(Car c,boolean includeAll) {
    this.id = c.getId();
    this.brand = c.getBrand();
    this.model = c.getModel();
    this.pricePrDay = c.getPricePrDay();
    if(includeAll) {
      this.bestDiscount = c.getBestDiscount();
      this.updateDateTime = c.getUpdateDateTime();
      this.createDateTime = c.getCreateDateTime();
    }
  }

}
