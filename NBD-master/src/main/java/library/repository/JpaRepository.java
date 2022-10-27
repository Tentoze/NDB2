package library.repository;

import java.util.List;

public interface JpaRepository<T> {
    T findById(Long id);

    T add(T entity);

    void remove(T entity);

    T update(T entity);

    long size();

    List<T> findAll();
}
