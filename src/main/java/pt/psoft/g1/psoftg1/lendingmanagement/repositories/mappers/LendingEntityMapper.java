package pt.psoft.g1.psoftg1.lendingmanagement.repositories.mappers;



import lombok.Builder;
import org.mapstruct.*;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel.LendingEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel.LendingNumberEntity;


@Mapper(componentModel = "spring")
public interface LendingEntityMapper {

    // Mapear de LendingEntity para Lending

    Lending sqlServerToModel(LendingEntity lendingEntity);

    // Mapear de Lending para LendingEntity

    LendingEntity modelToSqlServer(Lending lending);
    @Mapping(target = "lendingNumber", source = "value")
    LendingNumberEntity stringToLne (String value);
    @Mapping(target = "lendingNumber", source = "value")
    LendingNumber stringToLn (String value);

}
