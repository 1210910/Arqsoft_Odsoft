package pt.psoft.g1.psoftg1.lendingmanagement.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel.FineEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel.LendingNumberEntity;

@Mapper(componentModel = "spring")
public interface FineEntityMapper {

    Fine sqlServerToModel(FineEntity fineEntity);

    FineEntity modelToSqlServer(Fine fine);

    @Mapping(target = "lendingNumber", source = "value")
    LendingNumberEntity stringToLne (String value);
    @Mapping(target = "lendingNumber", source = "value")
    LendingNumber stringToLn (String value);



}
