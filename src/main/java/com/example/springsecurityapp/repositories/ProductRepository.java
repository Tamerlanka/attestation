package com.example.springsecurityapp.repositories;

import com.example.springsecurityapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

        // Поиск товара по наименованию или его части независимо от регистра
    List<Product> findByTitleContainingIgnoreCase(String name);

        // Поиск по наименованию и фильтрация по диапазону цен
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '?1%')) and (price >=?2 and price <=?3)",nativeQuery = true) /* lower(title) - приводим title (наименование)  в нижний регистр. Данный title может быть в начале, середине или конце строки. LIKE позволяет сравнить строку с какой-то конкретной маской. Мы проверяем наличие введенного title  в начале(?1%), середине (%?1%) или конце(?1%) строки (1 в маске это введенный title, 1-й параметр атрибутов, String name) ?2 это второй введенный атрибут (float ot), ?3 - это третий введенный атрибут (float Do) */
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String name, float ot, float Do);

    /* Поиск по наименованию и фильтрация по диапазону цен и сортировка по возрастанию цены Asc - по возрастанию Desc по убыванию, Asc ставить не надо так как он по умолчанию при запросе на сортировку, смотри метод ниже */
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '?1%') and (price >=?2 and price <=?3) order by price" ,nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float ot, float Do);

    /* Поиск по наименованию и фильтрация по диапазону цен и сортировка по убыванию цены Asc - по возрастанию Desc по убыванию, Asc ставить не надо так как он по умолчанию при запросе на сортировку, смотри метод выше */
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '?1%') and (price >=?2 and price <=?3) order by price desc" ,nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String title, float ot, float Do);

    // Поиск по наименованию и фильтрация по диапазону цен и сортировка по возрастанию цены а так же фильтрация по категории. ?4 - 4й атрибут (параметр) в методе
    @Query(value = "select * from product where category_id = ?4 and (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '?1%') and (price >=?2 and price <=?3) order by price" ,nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float ot, float Do, int category);

    // Поиск по наименованию и фильтрация по диапазону цен и сортировка по убыванию цены а так же фильтрация по категории. ?4 - 4й атрибут (параметр) в методе
    @Query(value = "select * from product where category_id = ?4 and (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '?1%') and (price >=?2 and price <=?3) order by price desc" ,nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float ot, float Do, int category);
}
























