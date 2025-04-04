package org.example.mealwise.dao;

import java.util.List;

public interface GenericDAO<T> {
    void insert(T obj);
    List<T> getAll();
    T getById(int id);
    boolean update(T obj);
    boolean delete(int id);
}
