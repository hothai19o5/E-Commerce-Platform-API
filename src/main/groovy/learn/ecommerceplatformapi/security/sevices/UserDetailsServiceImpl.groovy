// Service để tải thông tin user từ DB
package learn.ecommerceplatformapi.security.sevices

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.entity.User
import learn.ecommerceplatformapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository

    @Override
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username))

        return UserDetailsImpl.build(user)
    }
}