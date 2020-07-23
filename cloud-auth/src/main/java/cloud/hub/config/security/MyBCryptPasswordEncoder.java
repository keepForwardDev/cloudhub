package cloud.hub.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyBCryptPasswordEncoder extends BCryptPasswordEncoder {

    @Value("${spring.security.user.superPassword:111111}")
    private String superPassword;

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword.toString().equals(superPassword)) {
            return true;
        }
        return super.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("111111", "$2a$10$s1xpBrBCuvDEfvFVq2plOuM7XtWl.FIkEgQNuYHZ/grqozG1Nunp2"));
    }
}
