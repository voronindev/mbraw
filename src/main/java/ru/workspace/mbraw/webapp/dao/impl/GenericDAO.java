package ru.workspace.mbraw.webapp.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import ru.workspace.mbraw.webapp.dao.IGenericDAO;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class GenericDAO<T, ID extends Serializable> implements IGenericDAO<T, ID> {

    @Resource
    private SessionFactory sessionFactory;

    private final Class<T> persistentClass;

    protected GenericDAO() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(persistentClass);
    }


    @Override
    public void create(T entity) {
        getSession().save(entity);
    }

    @Override
    public void update(T entity) {
        getSession().update(entity);
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Override
    public T findById(ID id) {
        return (T) getSession().get(persistentClass, id);
    }

    @Override
    public List<T> findAll() {
        return getSession().createCriteria(persistentClass).list();
    }

    @Override
    public List<T> findWith(int limit, int offset) {
        return getSession().createCriteria(persistentClass).setFirstResult(offset).setMaxResults(limit).list();
    }

    @Override
    public int count() {
        return (Integer) sessionFactory.getCurrentSession().createCriteria(persistentClass).setProjection(
                Projections.rowCount()).uniqueResult();
    }
}
