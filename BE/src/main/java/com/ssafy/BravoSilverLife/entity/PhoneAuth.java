package com.ssafy.BravoSilverLife.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneAuth extends BaseTimeEntity{
    @Id
    String phoneNumber;
    String phoneAuth;
}
