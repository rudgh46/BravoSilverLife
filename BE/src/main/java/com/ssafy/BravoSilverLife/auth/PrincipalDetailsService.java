package com.ssafy.BravoSilverLife.auth;

import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User findMember = userRepository.findById(username);
        if(findMember != null){
            return new PrincipalDetails(findMember);
        }
        return null;
    }
}
