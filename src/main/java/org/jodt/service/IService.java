package org.jodt.service;

import org.jodt.models.ResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    ResponseDTO<List<T>> getAll();
    ResponseDTO<List<T>> getAll(Integer page, Integer offset);
    Optional<T> findById(Long id);
    T save(T t);
    T update(T t);
    void delete(Long id);
}
