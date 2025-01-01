package pt.psoft.g1.psoftg1.lendingmanagement.services;

import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingView;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface LendingService {

    Lending create(LendingViewAMQP lendingViewAMQP);
    Lending delete(LendingViewAMQP lendingViewAMQP);
    Lending update(LendingViewAMQP lendingViewAMQP);

}
