package dat3.car.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

@Entity
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @CreationTimestamp
  private LocalDate reservationDate;
  private LocalDate rentalDate;

  @ManyToOne
  private Member member;

  @ManyToOne
  private Car car;


  public Reservation(LocalDate rentalDate, Member member, Car car) {
    this.rentalDate = rentalDate;
    this.member = member;
    this.car = car;
  }
}

