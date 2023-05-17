package com.example.springsecurityapp.repositories;


import com.example.springsecurityapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    com.example.springsecurityapp.models.Category findByName(String name); // Данный метод будет возвращать категорию из таблицы БД по ее наименованию

}
