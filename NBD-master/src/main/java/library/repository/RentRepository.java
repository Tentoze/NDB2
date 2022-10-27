package library.repository;

import jakarta.persistence.EntityManager;
import library.model.Book;
import library.model.Client;
import library.model.Rent;

import java.util.List;

public class RentRepository extends JpaRepositoryImpl<Rent> {
    public RentRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Rent> findByClient(Client client){
        return entityManager.createNamedQuery("Rent.findByClient",Rent.class).setParameter("client",client).getResultList();
    }

    public List<Rent> findAll(){
        return entityManager.createNamedQuery("Rent.findAll",Rent.class).getResultList();
    }

    public Rent findByBook(Book book){
        return entityManager.createNamedQuery("Rent.findByBook",Rent.class).setParameter("book",book).getSingleResult();
    }
    public Long countByClient(Client client){
        return entityManager.createNamedQuery("Rent.countByClient",Long.class).setParameter("client",client).getSingleResult();
    }

    public boolean existsByBook(Book book){
        return entityManager.createNamedQuery("Rent.existsByBook",Boolean.class).setParameter("book",book).getSingleResult();
    }
}
