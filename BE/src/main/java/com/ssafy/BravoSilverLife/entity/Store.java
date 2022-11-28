package com.ssafy.BravoSilverLife.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    int id;
    String add1;
    String add2;
    String gu;
    String dong;
    String name;
    String category;
}
