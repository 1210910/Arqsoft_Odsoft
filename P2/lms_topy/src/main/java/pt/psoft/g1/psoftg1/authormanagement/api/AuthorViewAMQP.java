package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Description;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
@Schema(description = "A Author form AMQP communication")
public class AuthorViewAMQP {

    @NotNull
    private String name;

    @NotNull
    private String bio;

    @NotNull
    private String authorNumber;

    private String photoURI;

    @NotNull
    private Long version;

    @Getter
    @Setter
    private Map<String, Object> _links = new HashMap<>();




}
