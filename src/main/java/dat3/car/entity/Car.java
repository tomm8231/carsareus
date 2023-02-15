package dat3.car.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor

@Entity
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int CarId;

  @Column(name = "car_brand", length = 50, nullable = false)
  private String brand;
  @Column(name = "car_model", length = 60, nullable = false)
  private String model;
  @Column(name = "rental_price_day")
  private double pricePrDay;
  @Column(name = "max_discount")
  private Integer bestDiscount;

  @CreationTimestamp
  private LocalDateTime created;
  @UpdateTimestamp
  private LocalDateTime edited;

  @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
  private List<Reservation> reservations = new ArrayList<>();


  public Car(String brand, String model, double pricePrDay, Integer bestDiscount) {
    this.brand = brand;
    this.model = model;
    this.pricePrDay = pricePrDay;
    this.bestDiscount = bestDiscount;
  }
}
