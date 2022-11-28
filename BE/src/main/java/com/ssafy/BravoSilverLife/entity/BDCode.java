package com.ssafy.BravoSilverLife.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bd_code")
public class BDCode {

    @Id
    long code;
    String sido;
    String gugun;
    String name;

}
