package com.k48.stock_management_system.services;

import java.util.List;

public interface AbstractService<T> {

    T save(T oobject);
    T findById(Integer id);
    List<T> findAll();
    T delete(Integer id);
}
