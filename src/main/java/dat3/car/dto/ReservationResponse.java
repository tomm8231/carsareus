package dat3.car.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.car.entity.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ReservationResponse {
  private Integer id;
  private String memberUsername;
  private int carId;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate rentalDate;

  public ReservationResponse(Reservation reservation) {
    this.id = reservation.getId();
    this.memberUsername = reservation.getMember().getUsername();
    this.carId = reservation.getCar().getCarId();
  }

}
