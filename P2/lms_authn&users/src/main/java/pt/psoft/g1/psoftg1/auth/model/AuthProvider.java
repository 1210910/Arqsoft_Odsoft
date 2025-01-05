package pt.psoft.g1.psoftg1.auth.model;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;

import java.net.Authenticator;

public interface AuthProvider {
    Authentication authenticate(AuthRequest request) throws AuthenticationException;
}
