package pt.psoft.g1.psoftg1.auth.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.Map;

@Profile("azure")
@PropertySource({"classpath:application.properties"})
@Component
public class AzureAdProvider implements AuthProvider {



    @Value("${spring.security.oauth2.client.registration.azure.client-id}")
    private  String CLIENT_ID ;
    @Value("${spring.security.oauth2.client.registration.azure.client-secret}")
    private  String CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.azure.scope}")
    private  String SCOPE;
    @Value("${spring.security.oauth2.client.provider.azure.issuer-uri}")
    private  String TOKEN_URL;

    private final UserService userService;

    @Autowired
    @Lazy
    public AzureAdProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(AuthRequest request) throws AuthenticationException {
        try {
            // Obter o token de acesso usando as credenciais do usuário
            String accessToken = getAccessToken(request);



            // Decodificar o token ou chamar o Microsoft Graph para validar
            String email = extractEmailFromToken(accessToken);



            // Carregar os detalhes do usuário com base no email
            UserDetails userDetails = userService.loadUserByUsername(email);

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (Exception e) {
            throw new AuthenticationException("Falha na autenticação com o Microsoft Entra", e) {};
        }
    }

    private String getAccessToken(AuthRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String email = request.getUsername().split("@")[0]+"@trialforschool642.onmicrosoft.com";


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("scope", "https://graph.microsoft.com/.default");
        body.add("username", email);
        body.add("password", request.getPassword());


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response  = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class);



        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Falha ao obter o token de acesso: " + response.getStatusCode());
        }
    }

    private String extractEmailFromToken(String token) {
        // Para simplificação, usamos o Microsoft Graph para obter as informações do usuário
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://graph.microsoft.com/v1.0/me",
                HttpMethod.GET,
                entity,
                Map.class);



        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("mail"); // campo de email
        } else {
            throw new RuntimeException("Falha ao extrair email: " + response.getStatusCode());
        }
    }
}