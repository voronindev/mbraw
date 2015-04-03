package ru.workspace.mbraw.webapp.dao;

import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T, ID extends Serializable> {

    void create(T entity);

    void update(T entity);

    void delete(T entity);

    T findById(ID id);

    List<T> findAll();

    List<T> findWith(int limit, int offset);

    int count();

}
