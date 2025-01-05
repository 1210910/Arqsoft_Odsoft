package pt.psoft.g1.psoftg1.auth.model;


import com.google.api.client.json.Json;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;


import java.io.IOException;
import org.springframework.http.*;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;


@Profile("firebase")
@Component
public class FirebaseProvider implements AuthProvider{



    private static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyBf8RORJDrrCpikK5bFPvkaDoXW6yvqqfs";



    private final UserService userService;

    @Autowired
    public FirebaseProvider(UserService userService) {
        this.userService = userService;

    }


    @Override
    public Authentication authenticate(AuthRequest request) {
        try {
            JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);

            JSONObject response = (JSONObject) parser.parse(login(request));

            String idToken = response.getAsString("idToken");

            System.out.println(idToken);

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            System.out.println(decodedToken.getUid());
            String email = decodedToken.getEmail();

            // Aqui, você pode buscar o UserDetails com base no UID ou email
            UserDetails userDetails = userService.loadUserByUsername(email);
            System.out.println(userDetails.getUsername());

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (FirebaseAuthException e) {
            throw new AuthenticationException("Firebase Authentication failed", e) {};
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed", e) {};
        }
    }



    private String login(AuthRequest request) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Criar o corpo da requisição
        String requestJson = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", request.getUsername(), request.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            // Enviar a requisição POST
            ResponseEntity<String> response = restTemplate.exchange(
                    SIGN_IN_BASE_URL ,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Verifica o status da resposta
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // Retorna o corpo da resposta JSON
            } else {
                throw new RuntimeException("Failed to authenticate: " + response.getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }

    }
}
