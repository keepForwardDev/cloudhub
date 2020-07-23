package cloud.hub.Bean;

import com.cloud.hub.bean.ShiroUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 所有的用户属性保存在ShiroUser
 */
public class LoginUser extends ShiroUser implements UserDetails {

    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> resourcesSet = new HashSet<>();
        // 权限资源
        getResourcesCode().forEach(code -> {
            resourcesSet.add(new SimpleGrantedAuthority(code));
        });
        getRolesCode().forEach(code -> {
            // 角色是一种特殊的资源，必须加上ROLE_
            resourcesSet.add(new SimpleGrantedAuthority("ROLE_" + code));
        });
        return resourcesSet;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getUuid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled() == 1;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
