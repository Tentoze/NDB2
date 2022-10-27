package library.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import library.model.Book;
import library.model.Client;

import java.util.List;

public class ClientRepository extends JpaRepositoryImpl<Client>{
    public ClientRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Client findByPersonalID(String personalID){
        return entityManager.createNamedQuery("Client.findByPersonalID",Client.class).setParameter("personalID",personalID).getSingleResult();
    }

    public List<Client> findAll(){
        return entityManager.createNamedQuery("Client.findAll",Client.class).getResultList();
    }




}
