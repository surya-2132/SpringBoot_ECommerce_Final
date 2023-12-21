package com.fullstackproject.ecommerce.Repository;

import com.fullstackproject.ecommerce.Model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    @Query("{id: \"?0\"}")
    List<Category> findByID(ObjectId objectId);

    @Query("{name: \"?0\"}")
    List<Category> findByName(String name);

    @Query("{description: \"?0\"}")
    List<Category> findByDesc(String desc);
}
