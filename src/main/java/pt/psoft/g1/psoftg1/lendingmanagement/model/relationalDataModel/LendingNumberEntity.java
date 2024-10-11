package pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;

/**
 * The LendingNumberEntity class is the data model responsible for persisting LendingNumber in the database.
 * It extends the LendingNumber model, inheriting the business logic while adding database mapping annotations.
 */
@Embeddable
public class LendingNumberEntity extends LendingNumber {

    @Column(name = "LENDING_NUMBER", length = 32)
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String lendingNumber;

    /**
     * Default constructor for JPA/ORM use.
     * Required for ORM frameworks.
     */
    protected LendingNumberEntity() {
        super(0); // Passing a default sequential value to satisfy the superclass
        this.lendingNumber = null; // ORM requires this for entity instantiation
    }

    /**
     * Constructs a LendingNumberEntity using year and sequential number.
     *
     * @param year       Year component of the LendingNumber
     * @param sequential Sequential component of the LendingNumber
     */
    public LendingNumberEntity(int year, int sequential) {
        super(year, sequential);  // Call to the base class constructor
        this.lendingNumber = super.getLendingNumber();
    }

    /**
     * Constructs a LendingNumberEntity using an existing LendingNumber string.
     *
     * @param lendingNumber String containing the lending number.
     */
    public LendingNumberEntity(String lendingNumber) {
        super(lendingNumber);  // Call to the base class constructor
        this.lendingNumber = super.getLendingNumber();
    }

    /**
     * Constructs a LendingNumberEntity using just the sequential number.
     * The year is set to the current year.
     *
     * @param sequential Sequential component of the LendingNumber
     */
    public LendingNumberEntity(int sequential) {
        super(sequential);  // Call to the base class constructor
        this.lendingNumber = super.getLendingNumber();
    }

    @Override
    public String toString() {
        return this.lendingNumber;
    }
}
