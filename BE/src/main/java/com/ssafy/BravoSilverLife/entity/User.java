package com.ssafy.BravoSilverLife.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotEmpty
    @Column(length = 50, nullable = false, unique = true)
    private String id;

    @OneToMany(mappedBy = "user")
    List<Bookmark> bookmark;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //JSON 결과로 출력하지 않을데이터에 대해 설정 비밀번호는 유출되면 안되니까
    @NotEmpty
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Column(length = 20, nullable = false, unique = true)
    private String nickname;


    @Column
    private String refreshToken;


    @Column
    private String phoneNumber;




    @Builder
    public User(String id, String password, String nickname,String phoneNumber ) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public void giveToken(String refreshToken) {this.refreshToken = refreshToken; }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changePassword(String newpassword){
        this.password=newpassword;
    }
    public void changePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //계정의 ID 리턴
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.id;
    }

    //계정이 만료됐는지 리턴
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 리턴
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료됐는지 리턴
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화돼 있는지 리턴
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void resetPassword(String password) {
        this.password = password;
    }

}
