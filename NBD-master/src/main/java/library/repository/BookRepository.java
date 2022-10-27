package library.repository;

import jakarta.persistence.*;
import library.model.Book;

import java.util.List;


public class BookRepository extends JpaRepositoryImpl<Book> {

    public BookRepository(EntityManager entityManager) {
        super(entityManager);
    }


    public Book findBySerialNumber(String serialNumber){
        return entityManager.createNamedQuery("Book.findBySerialNumber",Book.class).setParameter("serialNumber",serialNumber).getSingleResult();
    }
    public List<Book> findAll(){
        return entityManager.createNamedQuery("Book.findAll",Book.class).getResultList();
    }

}
