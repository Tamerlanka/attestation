package com.example.springsecurityapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity             // Указываем что это модель
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //Любая модель должна иметь первичный ключ
    @Column(name="title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Наименование товара не может быть пустым")
    private String title;
    @Column(name="description", nullable = false, columnDefinition = "text")
    @NotEmpty(message="Описание товара не может быть пустым")
    private String description;
    @Column(name="price", nullable = false)
    @Min(value = 1, message = "Цена товара не может быть отрицательной или нулевой")
    private float price;
    @Column(name="warehouse", nullable = false)
    @NotEmpty(message = "Поле Склад не может быть пустым")
    private String warehouse;
    @Column(name="seller", nullable = false)
    @NotEmpty(message = "Поле информации о продавце не может быть пустым")
    private String seller;
    @ManyToOne(optional = false) // связь с моделью Category
    private Category category;
    private LocalDateTime dateTime; // Время регистрации товара в системе заполняется с помощью метода init() данного класса.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")  // Связь с моделью Image
    private List<Image> imageList = new ArrayList<>(); //Связь с моделью Image храним лист изображений товара

    @ManyToMany()   // связь многие ко многим Это связь с корзиной.
    @JoinTable(name="product_cart", joinColumns = @JoinColumn(name="product_id"), inverseJoinColumns = @JoinColumn(name="person_id")) // создаем 3-ю промежуточную таблицу product_cart, в которой будет колонка product_id (отвечает за текущую модель) и колонка person_id. Точно такую же связь надо прописать в Person(model) только вместо personList будет productList и поменяются местами поля.
    private List<Person> personList;
    @OneToMany(mappedBy = "product", fetch=FetchType.EAGER)
    private List<Order> orderList;

    public Product(String title, String description, float price, String warehouse, String seller, Category category, LocalDateTime dateTime, List<Image> imageList) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.category = category;
        this.dateTime = dateTime;
        this.imageList = imageList;
    }

    public Product() {
    }

    // Метод для добавления изображений товара.
    public void addImageToProduct(Image image){ // Получаем в аргументах объект класса Image
        image.setProduct(this); // Устанавливаем через объект класса Image привязку к текущему продукту (кладем в поле product класса Image)
        imageList.add(image);   // кладем объект image в imageList  класса Product
    }

    /* Метод для заполнения поля Даты и Времени на момент создания объекта данного класса,
     срабатывает автоматически в момент создания экземпляра класса */
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
