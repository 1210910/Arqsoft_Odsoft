package pt.psoft.g1.psoftg1.lendingmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Schema(description = "A Lending form AMQP communication")
public class LendingViewAMQP {

    @NotNull
    private String lendingNumber;

    @NotNull
    private String genId;

    @NotNull
    private String isbn;

    @NotNull
    private String readerNumber;

    @NotNull
    private String startDate;

    @NotNull
    private String limitDate;

    private String returnDate;

    private String daysUntilReturn;

    private String daysOverdue;

    private String fineValueInCents;

    @Getter
    @Setter
    private Map<String, Object> _links = new HashMap<>();
}
