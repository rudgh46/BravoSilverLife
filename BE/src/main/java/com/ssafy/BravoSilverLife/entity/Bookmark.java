package com.ssafy.BravoSilverLife.entity;

import com.ssafy.BravoSilverLife.dto.StoreDto;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    User user;

    long articleNo;
    String address;
    String url;
    String price;
    String month;
}
