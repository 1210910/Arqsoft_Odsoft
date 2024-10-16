package pt.psoft.g1.psoftg1.lendingmanagement.repositories.relational.sqlServer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.lendingmanagement.model.relationalDataModel.LendingEntity;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.mappers.LendingEntityMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LendingRepositorySqlServerImpl implements LendingRepository {

    private final LendingRepositorySqlServer lendingRepositorySqlServer;
    private final LendingEntityMapper lendingEntityMapper;

    @PersistenceContext
    private final EntityManager em;


    @Autowired
    @Lazy
    public LendingRepositorySqlServerImpl(LendingRepositorySqlServer lendingRepositorySqlServer, LendingEntityMapper lendingEntityMapper, EntityManager em) {
        this.lendingRepositorySqlServer = lendingRepositorySqlServer;
        this.lendingEntityMapper = lendingEntityMapper;
        this.em = em;
    }

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {

        return lendingRepositorySqlServer.findByLendingNumber(lendingNumber);

    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        // Exemplo de delegação de uma busca
        return this.lendingRepositorySqlServer.listByReaderNumberAndIsbn(readerNumber,isbn); // Implementar ou delegar para o repo JPA
    }

    @Override
    public int getCountFromCurrentYear() {
        // Exemplo de lógica customizada
        return this.lendingRepositorySqlServer.getCountFromCurrentYear();
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        return this.lendingRepositorySqlServer.listOutstandingByReaderNumber(readerNumber);
    }

    @Override
    public Double getAverageDuration() {
        return this.lendingRepositorySqlServer.getAverageDuration();
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        return this.lendingRepositorySqlServer.getAvgLendingDurationByIsbn(isbn);
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        final Root<Lending> root = cq.from(Lending.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        // Select overdue lendings where returnedDate is null and limitDate is before the current date
        where.add(cb.isNull(root.get("returnedDate")));
        where.add(cb.lessThan(root.get("limitDate"), LocalDate.now()));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("limitDate"))); // Order by limitDate, oldest first

        final TypedQuery<Lending> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        final Root<Lending> lendingRoot = cq.from(Lending.class);
        final Join<Lending, Book> bookJoin = lendingRoot.join("book");
        final Join<Lending, ReaderDetails> readerDetailsJoin = lendingRoot.join("readerDetails");
        cq.select(lendingRoot);

        final List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(readerNumber))
            where.add(cb.like(readerDetailsJoin.get("readerNumber").get("readerNumber"), readerNumber));
        if (StringUtils.hasText(isbn))
            where.add(cb.like(bookJoin.get("isbn").get("isbn"), isbn));
        if (returned != null){
            if(returned){
                where.add(cb.isNotNull(lendingRoot.get("returnedDate")));
            }else{
                where.add(cb.isNull(lendingRoot.get("returnedDate")));
            }
        }
        if(startDate!=null)
            where.add(cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), startDate));
        if(endDate!=null)
            where.add(cb.lessThanOrEqualTo(lendingRoot.get("startDate"), endDate));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(lendingRoot.get("lendingNumber")));

        final TypedQuery<Lending> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public Lending save(Lending lending) {
        // Converte Lending para LendingEntity
        LendingEntity entity = lendingEntityMapper.modelToSqlServer(lending);
        System.out.println(entity.getLendingNumber());
        LendingEntity savedEntity = lendingRepositorySqlServer.save(entity); // delega o save para o JPA
        return lendingEntityMapper.sqlServerToModel(savedEntity);
    }




}
