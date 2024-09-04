package kroryi.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "address")
    private String address;

    @Column(name = "zipcode")
    private String zipCode;

}
