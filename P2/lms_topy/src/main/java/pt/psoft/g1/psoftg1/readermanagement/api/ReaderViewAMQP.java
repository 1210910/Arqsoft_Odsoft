package pt.psoft.g1.psoftg1.readermanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "A Reader form AMQP communication")
public class ReaderViewAMQP {

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String readerNumber;

    @NotNull
    private String password;

    private String birthDate;

    private String phoneNumber;

    private String photoURL;

    private boolean gdpr;

    private boolean marketing;

    private boolean thirdParty;

    private String version;

    private List<String> interestList;

    @Getter
    @Setter
    private Map<String, Object> _links = new HashMap<>();


}
