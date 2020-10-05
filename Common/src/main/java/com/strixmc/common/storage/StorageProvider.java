package com.strixmc.common.storage;


import com.strixmc.common.model.Model;

import java.util.Optional;

public interface StorageProvider<T extends Model> extends Model {

  Optional<T> find(String id);

  void loadAll();

  boolean exist(String id);

  void save(T object);

  void saveAll();

  void delete(String id);

  default void delete(T object) {
    delete(object.getId());
  }

}
