package com.example.springsecurityapp.repositories;

import com.example.springsecurityapp.models.Order;
import com.example.springsecurityapp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);
}
