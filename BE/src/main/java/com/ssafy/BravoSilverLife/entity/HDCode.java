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
@Table(name = "hd_code")
public class HDCode {
    @Id
    int code;
    String name;
}
