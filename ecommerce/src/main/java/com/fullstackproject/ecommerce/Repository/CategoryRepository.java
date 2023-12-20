package com.fullstackproject.ecommerce.Repository;

import com.fullstackproject.ecommerce.Model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
}
