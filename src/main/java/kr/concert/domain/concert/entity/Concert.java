package kr.concert.domain.concert.entity;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


//@Table(name = "concert")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="concert")
public class Concert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id" , nullable = false)
    private Long concertId;

    @Column(name = "concert_name", nullable = false)
    private String concertName;

}
