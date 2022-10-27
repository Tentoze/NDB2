package library.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import library.model.AbstractEntity;

import java.util.List;

public class JpaRepositoryImpl<T extends AbstractEntity> implements JpaRepository<T>{
    @PersistenceContext
    protected EntityManager entityManager;

    public JpaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T findById(Long id) {
        return null;
    }

    @Override
    public T add(T entity) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(entity);
        entityTransaction.commit();
        return entity;
    }

    @Override
    public void remove(T entity) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.remove(entity);
        entityTransaction.commit();
    }

    @Override
    public T update(T entity) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.merge(entity);
        entityTransaction.commit();
        return entity;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public List<T> findAll() {
        return null;
    }
}
