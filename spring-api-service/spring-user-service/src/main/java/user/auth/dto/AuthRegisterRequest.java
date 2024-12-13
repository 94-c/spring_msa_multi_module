package user.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter @Setter
public class AuthRegisterRequest {
    private String email;
    private String password;

    public AuthRegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static AuthRegisterRequest of(String email, String password) {
        return new AuthRegisterRequest(email, password);
    }

}
