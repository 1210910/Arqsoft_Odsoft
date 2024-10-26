package pt.psoft.g1.psoftg1.shared.repositories.mappers;

import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.model.mongodb.ForbiddenNameMongoDB;

public interface ForbiddenNameMapperMongoDB {

    ForbiddenName toDomain(ForbiddenNameMongoDB forbiddenNameMongoDB);

    ForbiddenNameMongoDB toMongoDB(ForbiddenName forbiddenName);
}
