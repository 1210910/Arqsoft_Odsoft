package pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@code LendingEntity} class defines the persistence model for the Lending system.
 * It extends the {@link Lending} class and adds JPA-specific annotations for database operations.
 */
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"LENDING_NUMBER"})})
@NoArgsConstructor
public class LendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Embedded
    private LendingNumberEntity lendingNumberEntity; // Reference to the embedded LendingNumberEntity


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

    @Size(min = 0, max = 1024)
    @Column(length = 1024)
    private String commentary = null;

    @Transient
    private int fineValuePerDayInCents;
    @Transient
    private Integer daysUntilReturn;


    private Integer daysOverdue;

    /**
     * Constructs a new {@code Lending} object.
     *
     * @param book             {@code Book} object, which should be retrieved from the database.
     * @param readerDetails    {@code Reader} object, which should be retrieved from the database.
     * @param seq              sequential number, which should be obtained from the year's count on the database.
     * @param lendingDuration  duration for the lending in days.
     * @param fineValuePerDayInCents fine value per day in cents.
     * @throws NullPointerException if any of the arguments is {@code null}
     */
    public LendingEntity(Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents) {
        try {
            this.book = Objects.requireNonNull(book);
            this.readerDetails = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.lendingNumberEntity = new LendingNumberEntity(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
    }
    @Builder
    public LendingEntity(Book book, ReaderDetails readerDetails, LendingNumberEntity lendingNumber, LocalDate startDate, LocalDate limitDate, LocalDate returnedDate, int fineValuePerDayInCents) {
        try {
            this.book = Objects.requireNonNull(book);
            this.readerDetails = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.lendingNumberEntity = lendingNumber;
        this.startDate = startDate;
        this.limitDate = limitDate;
        this.returnedDate = returnedDate;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
    }

    public void setReturned(final long desiredVersion, final String commentary) {
        if (this.returnedDate != null) {
            throw new IllegalArgumentException("Book has already been returned!");
        }

        // Check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.lendingNumberEntity);
        }

        if (commentary != null) {
            this.commentary = commentary;
        }

        this.returnedDate = LocalDate.now();
    }

    public int getDaysDelayed() {
        if (this.returnedDate != null) {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, this.returnedDate), 0);
        } else {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, LocalDate.now()), 0);
        }
    }

    private void setDaysUntilReturn() {
        int daysUntilReturn = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.limitDate);
        this.daysUntilReturn = (this.returnedDate != null || daysUntilReturn < 0) ? null : daysUntilReturn;
    }

    private void setDaysOverdue() {
        int days = getDaysDelayed();
        this.daysOverdue = (days > 0) ? days : null;
    }

    public Optional<Integer> getDaysUntilReturn() {
        setDaysUntilReturn();
        return Optional.ofNullable(daysUntilReturn);
    }

    public Optional<Integer> getDaysOverdue() {
        setDaysOverdue();
        return Optional.ofNullable(daysOverdue);
    }

    public Optional<Integer> getFineValueInCents() {
        Optional<Integer> fineValueInCents = Optional.empty();
        int days = getDaysDelayed();
        if (days > 0) {
            fineValueInCents = Optional.of(fineValuePerDayInCents * days);
        }
        return fineValueInCents;
    }

    public String getTitle() {
        return this.book.getTitle().toString();
    }

    public String getLendingNumber() {
        return this.lendingNumberEntity.toString();
    }



    /** Factory method for bootstrapping. */
    public static LendingEntity newBootstrappingLending(Book book, ReaderDetails readerDetails,
                                                  int year, int seq, LocalDate startDate,
                                                  LocalDate returnedDate, int lendingDuration,
                                                  int fineValuePerDayInCents) {
        LendingEntity lending = new LendingEntity();

        try {
            lending.book = Objects.requireNonNull(book);
            lending.readerDetails = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        lending.lendingNumberEntity = new LendingNumberEntity(year, seq);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.fineValuePerDayInCents = fineValuePerDayInCents;
        lending.returnedDate = returnedDate;
        return lending;
    }

}
