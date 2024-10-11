package pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;

/**
 * The {@code FineEntity} class defines the data model for the Fine in the database.
 * It extends the {@link Fine} class and adds persistence logic through JPA.
 * */
@Getter
@Entity
@Table(name = "fine")
public class FineEntity extends Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    /** Fine value per day in cents is persisted but not updatable */
    @PositiveOrZero
    @Column(updatable = false)
    private int fineValuePerDayInCents;

    /** Fine value in Euro cents */
    @PositiveOrZero
    private int centsValue;

    @Setter
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "lending_pk", nullable = false, unique = true)
    private LendingEntity lendingEntity;

    /** Protected empty constructor for ORM only. */
    protected FineEntity() {
        super(); // Default constructor for ORM
    }

    /**
     * Constructs a new {@code FineEntity} object by calling the parent {@code Fine} constructor.
     *
     * @param lendingEntity transaction which generates this fine.
     */
    public FineEntity(LendingEntity lendingEntity) {
        super(lendingEntity); // Calls the Fine logic
        this.lendingEntity = lendingEntity;
    }
}
