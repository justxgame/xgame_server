package com.xgame.order.consumer.db.dao;

import java.util.List;

/**
 */

public interface BaseDao<T> {

    /**
     * save an entity
     *
     * @param obj
     */
    void saveObject(T obj);

    T getObjectByID(Object id);

    void updateObjectById(Object dto);

    List<T> getAll();

    void deleteById(Object id);

    void saveObjects(List<T> obj);

}
