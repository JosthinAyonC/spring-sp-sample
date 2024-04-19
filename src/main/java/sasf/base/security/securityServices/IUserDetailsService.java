package sasf.base.security.securityServices;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sasf.base.entity.User;
import sasf.base.repository.UserRepo;

@Service
public class IUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            if (userRepository.existsByEmail(username)) {
                user = userRepository.findByEmail(username);
            } else {
                throw new UsernameNotFoundException(
                        "No se ha encontrado el usuario con email o nombre de usuario: " + username);
            }
        }
        return IUserDetails.build(user);
    }

}
