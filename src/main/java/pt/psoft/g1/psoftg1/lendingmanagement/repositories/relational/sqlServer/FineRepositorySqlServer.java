package pt.psoft.g1.psoftg1.lendingmanagement.repositories.relational.sqlServer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel.FineEntity;

import java.util.Optional;

public interface FineRepositorySqlServer extends CrudRepository<FineEntity, Long> {

    @Query("SELECT f " +
            "FROM FineEntity f " +
            "JOIN LendingEntity l ON f.lendingEntity.pk = l.pk " +
            "WHERE l.lendingNumberEntity.lendingNumber = :lendingNumber")
    Optional<Fine> findByLendingNumber(String lendingNumber);

}
