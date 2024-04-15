package org.jodt.repository;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll();
    List<T> getAll(Integer limit, Integer offset);
    T findById(Long id);
    T save(T t);
    T update(T t);
    void delete(Long id);
}
