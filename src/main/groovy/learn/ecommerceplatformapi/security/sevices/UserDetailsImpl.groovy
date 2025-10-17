// Lớp biểu diễn thông tin người dùng
package learn.ecommerceplatformapi.security.sevices

import com.fasterxml.jackson.annotation.JsonIgnore
import learn.ecommerceplatformapi.entity.Role
import learn.ecommerceplatformapi.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl implements UserDetails {

    Long id
    String username
    String email
    @JsonIgnore
    String password
    Collection<? extends GrantedAuthority> authorities

    UserDetailsImpl(Long id, String username, String email, String password,
                    Collection<? extends GrantedAuthority> authorities) {
        this.id = id
        this.username = username
        this.email = email
        this.password = password
        this.authorities = authorities
    }

    static UserDetailsImpl build(User user) {
        List<SimpleGrantedAuthority> authorities = user.roles.collect { Role r ->
            new SimpleGrantedAuthority(r.name.name())
        }
        return new UserDetailsImpl(
                user.id,
                user.username,
                user.email,
                user.password,
                authorities
        )
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() { authorities }

    Long getId() { id }

    String getEmail() { email }

    @Override
    String getPassword() { password }

    @Override
    String getUsername() { username }

    @Override
    boolean isAccountNonExpired() { true }

    @Override
    boolean isAccountNonLocked() { true }

    @Override
    boolean isCredentialsNonExpired() { true }

    @Override
    boolean isEnabled() { true }

    @Override
    boolean equals(Object o) {
        if (this.is(o)) return true
        if (!(o instanceof UserDetailsImpl)) return false
        return id != null && id == ((UserDetailsImpl) o).id
    }
}
