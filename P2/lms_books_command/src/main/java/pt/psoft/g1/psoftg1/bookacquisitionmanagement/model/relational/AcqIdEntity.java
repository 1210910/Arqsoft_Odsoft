package pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.relational;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Embeddable
@EqualsAndHashCode
public class AcqIdEntity implements Serializable {
    @Size(min = 10, max = 13)
    @Column(name="ACQID", length = 16)

    String acqID;

    public AcqIdEntity(String acqID) {
        if (isValidAcqID(acqID)) {
            this.acqID = acqID;
        } else {
            throw new IllegalArgumentException("Invalid ACQID-13 format or check digit.");
        }
    }

    protected AcqIdEntity() {};

    private static boolean isValidAcqID(String acqID) {
        if(acqID == null)
            throw new IllegalArgumentException("Acquisition ID cannot be null");
        return (acqID.length() == 10) ? isValidAcqID10(acqID) : isValidAcqID13(acqID);
    }

    private static boolean isValidAcqID10(String acqID) {
        if (!acqID.matches("\\d{9}[\\dX]")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (acqID.charAt(i) - '0') * (10 - i);
        }

        char lastChar = acqID.charAt(9);
        int lastDigit = (lastChar == 'X') ? 10 : lastChar - '0';
        sum += lastDigit;

        return sum % 11 == 0;
    }

    private static boolean isValidAcqID13(String acqID) {
        if (acqID == null || !acqID.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Integer.parseInt(acqID.substring(i, i + 1));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = 10 - (sum % 10);
        if (checksum == 10) {
            checksum = 0;
        }

        return checksum == Integer.parseInt(acqID.substring(12));
    }

    public String toString() {
        return this.acqID;
    }
}
