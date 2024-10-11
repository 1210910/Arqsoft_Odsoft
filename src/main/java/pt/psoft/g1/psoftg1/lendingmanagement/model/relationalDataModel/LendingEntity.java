package pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;

/**
 * The {@code LendingEntity} class defines the persistence model for the Lending system.
 * It extends the {@link Lending} class and adds JPA-specific annotations for database operations.
 */
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"LENDING_NUMBER"})})
public class LendingEntity extends Lending {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lendingNumber", column = @Column(name = "LENDING_NUMBER", nullable = false, unique = true))
    })
    private LendingNumberEntity lendingNumber; // Reference to the embedded LendingNumberEntity


    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Book book;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ReaderDetails readerDetails;


    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate limitDate;

    @Temporal(TemporalType.DATE)
    private LocalDate returnedDate;

    @Version
    private long version;

    protected LendingEntity() {
        super(); // Default constructor for ORM
    }

    /**
     * Constructs a new {@code LendingEntity} object by calling the parent {@code Lending} constructor.
     *
     * @param book the book being lent.
     * @param readerDetails the reader borrowing the book.
     * @param lendingNumber the unique lending number.
     * @param seq sequential number for the lending.
     * @param lendingDuration the lending duration in days.
     * @param fineValuePerDayInCents fine value per overdue day.
     */
    public LendingEntity(Book book, ReaderDetails readerDetails, LendingNumberEntity lendingNumber, int seq, int lendingDuration, int fineValuePerDayInCents) {
        super(book, readerDetails, seq, lendingDuration, fineValuePerDayInCents);
        this.lendingNumber = lendingNumber; // Initialize the lending number
    }

    /**
     * Factory method for bootstrapping.
     */
    public static LendingEntity newBootstrappingLending(Book book, ReaderDetails readerDetails, LendingNumberEntity lendingNumber, int year, int seq,
                                                        LocalDate startDate, LocalDate returnedDate, int lendingDuration,
                                                        int fineValuePerDayInCents) {
        LendingEntity lendingEntity = new LendingEntity(book, readerDetails, lendingNumber, seq, lendingDuration, fineValuePerDayInCents);
        lendingEntity.startDate = startDate;
        lendingEntity.limitDate = startDate.plusDays(lendingDuration);
        lendingEntity.returnedDate = returnedDate;
        return lendingEntity;
    }
}
