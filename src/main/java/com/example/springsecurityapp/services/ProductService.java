package com.example.springsecurityapp.services;

import com.example.springsecurityapp.models.Category;
import com.example.springsecurityapp.models.Product;
import com.example.springsecurityapp.repositories.ProductRepository;
import jakarta.servlet.http.PushBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService { // Сервисный слой для репозитория
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

        // Метод для получения списка всех товаров.
        public List<Product> getAllProduct(){
            return productRepository.findAll();
        }

        // Метод для получения товара по id.
        public Product getProductId(int id){
            Optional<Product> optionalProduct = productRepository.findById(id);
            return optionalProduct.orElse(null);
        }

        // Метод для сохранения нового товара в БД.
        @Transactional
        public void saveProduct(Product product, Category category){
            product.setCategory(category);
            productRepository.save(product);
        }

        // Метод для обновления  в БД информации о товаре.
        @Transactional
        public void updateProduct(int id, Product product){
            product.setId(id);
            productRepository.save(product);
        }

        // Метод для удаления товара по id.
        @Transactional
        public void deleteProduct(int id){
            productRepository.deleteById(id);
        }
}
