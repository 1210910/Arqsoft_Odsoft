package pt.psoft.g1.psoftg1.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;
import pt.psoft.g1.psoftg1.auth.model.AuthProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProvider authProvider;

    public Authentication authenticate(AuthRequest request) {
        return authProvider.authenticate(request);
    }
}
