package org.jodt.repository;

import org.jodt.models.ResponseDTO;

import java.util.List;

public interface IRepository<T> {
    ResponseDTO<List<T>> getAll();
    ResponseDTO<List<T>> getAll(Integer limit, Integer offset);
    T findById(Long id);
    T save(T t);
    T update(T t);
    void delete(Long id);
    Integer getCount();
}
