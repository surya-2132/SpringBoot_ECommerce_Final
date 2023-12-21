package com.fullstackproject.ecommerce.Repository;

import com.fullstackproject.ecommerce.Model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    @Query("{username: \"?0\"}")
    List<User> getUserByUserName(String username);

    //SELECT * FROM user WHERE id = :id
    @Query("{id: \"?0\"}")
    Optional<User> getUserById(ObjectId id);

    List<User> findByEmail(String email);

    List<User> findByPassWord1(String passWord1);

    List<User> findByPassWord2(String passWord1);
}
