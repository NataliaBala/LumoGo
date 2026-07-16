package pl.lumogo.backend.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lumogo.backend.model.AppUser;
import pl.lumogo.backend.repository.AppUserRepository;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + email));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(List.of(authority))
            .build();
    }
}
