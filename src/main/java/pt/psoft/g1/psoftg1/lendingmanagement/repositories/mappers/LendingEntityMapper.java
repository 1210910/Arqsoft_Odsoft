package pt.psoft.g1.psoftg1.lendingmanagement.repositories.mappers;



import org.mapstruct.*;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.model.relational.TitleEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relational.LendingEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relational.LendingNumberEntity;
import pt.psoft.g1.psoftg1.shared.model.Photo;


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

    default String map(Photo value) {
        if (value == null) {
            return null;
        }
        return value.getPhotoFile(); // Exemplo para Photo
    }

    default String map(TitleEntity value) {
        if (value == null) {
            return null;
        }
        return value.getTitle(); // Exemplo para TitleEntity
    }

    default String map(Title value) {
        if (value == null) {
            return null;
        }
        return value.getTitle();  // Exemplo para Title
    }

}
