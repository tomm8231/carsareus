package dat3.car.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Member {

  @Id
  private String username;

  private String password;
  private String firstName;
  private String email;
  private String lastName;
  private String street;
  private String city;
  private String zip;

  private boolean approved;
  private int ranking;

  @CreationTimestamp
  private LocalDateTime created;
  @UpdateTimestamp
  private LocalDateTime lastEdited;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Reservation> reservations = new ArrayList<>();



  public Member(String user, String password, String email,
                String firstName, String lastName, String street, String city, String zip) {
    this.username = user;
    this.password= password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.street = street;
    this.city = city;
    this.zip = zip;
  }



}
