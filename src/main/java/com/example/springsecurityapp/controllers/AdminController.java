package com.example.springsecurityapp.controllers;

import com.example.springsecurityapp.models.Category;
import com.example.springsecurityapp.models.Image;
import com.example.springsecurityapp.models.Product;
import com.example.springsecurityapp.repositories.CategoryRepository;
import com.example.springsecurityapp.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AdminController {  // Контроллер под шаблон admin

    private final ProductService productService;


    @Value("${upload.path}") // внедряем значение(путь) из application.properties
    private String uploadPath;


    private final CategoryRepository categoryRepository;

    public AdminController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProduct()); // Делаем все продукты доступными на страничке админа
        return "admin";
    }
    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id)); // Находим продукт по id
        model.addAttribute("category", categoryRepository.findAll());   // кладем список категорий вдруг пригодятся
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String aditProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }


    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product()); // Кладем в модель объект продукта который привязан к форме
        model.addAttribute("category", categoryRepository.findAll()); // Добавляем в модель объект категории
        return "product/addProduct";
    }

    /* Метод принимает из формы объект модели привязанной к форме - "product" и сохраняет ее в экземпляр product через валидацию @Valid, экземпляр bindingResult в который сохраняются ошибки после валидации и пять файлов, которые не привязаны к форме и должны нами получаться отдельно, они принимаются через @RequestParam и кладутся в экземпляры класса MultipartFileа так же category в переменную category и экземпляр модели */
    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two") MultipartFile file_two, @RequestParam("file_three") MultipartFile file_three, @RequestParam("file_four") MultipartFile file_four, @RequestParam("file_five") MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = categoryRepository.findById(category).orElseThrow(); //Создаем объект для категории, через объект categoryRepository обращаемся к методу findById() в который кладем категорию и если категории не окажется выбрасываем исключение.
        System.out.println(category_db.getName()); // проверка не нужная строка
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }
        if(file_one != null){
            File uploadDir = new File(uploadPath); // Превращаем ссылку на файл в объект файла
            if(!uploadDir.exists()){            // Проверяем существует ли директория
                uploadDir.mkdir();              // Если не существует - создаем
            }
                String uuidFile = UUID.randomUUID().toString();     // генерируем уникальную строку
                String resultFileName = uuidFile + "." + file_one.getOriginalFilename();  // формируем уникальное внутреннее имя файла
                file_one.transferTo(new File(uploadPath + "/" + resultFileName));  // Сохраняем (перемещаем) файл
                Image image = new Image();  // создаем объект image
                image.setProduct(product); //  привязываем к image продукт с которым работаем
                image.setFileName(resultFileName);  // указываем имя фото как имя файла
                product.addImageToProduct(image);  // добавляем image  в лист изображений продукта
        }
        if(file_two != null){
            File uploadDir = new File(uploadPath); // Превращаем ссылку на файл в объект файла
            if(!uploadDir.exists()){            // Проверяем существует ли директория
                uploadDir.mkdir();              // Если не существует - создаем
            }
            String uuidFile = UUID.randomUUID().toString();     // генерируем уникальную строку
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();  // формируем уникальное внутреннее имя файла
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));  // Сохраняем (перемещаем) файл
            Image image = new Image();  // создаем объект image
            image.setProduct(product); //  привязываем к image продукт с которым работаем
            image.setFileName(resultFileName);  // указываем имя фото как имя файла
            product.addImageToProduct(image);  // добавляем image  в лист изображений продукта
        }
        if(file_three != null){
            File uploadDir = new File(uploadPath); // Превращаем ссылку на файл в объект файла
            if(!uploadDir.exists()){            // Проверяем существует ли директория
                uploadDir.mkdir();              // Если не существует - создаем
            }
            String uuidFile = UUID.randomUUID().toString();     // генерируем уникальную строку
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();  // формируем уникальное внутреннее имя файла
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));  // Сохраняем (перемещаем) файл
            Image image = new Image();  // создаем объект image
            image.setProduct(product); //  привязываем к image продукт с которым работаем
            image.setFileName(resultFileName);  // указываем имя фото как имя файла
            product.addImageToProduct(image);  // добавляем image  в лист изображений продукта
        }
        if(file_four != null){
            File uploadDir = new File(uploadPath); // Превращаем ссылку на файл в объект файла
            if(!uploadDir.exists()){            // Проверяем существует ли директория
                uploadDir.mkdir();              // Если не существует - создаем
            }
            String uuidFile = UUID.randomUUID().toString();     // генерируем уникальную строку
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();  // формируем уникальное внутреннее имя файла
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));  // Сохраняем (перемещаем) файл
            Image image = new Image();  // создаем объект image
            image.setProduct(product); //  привязываем к image продукт с которым работаем
            image.setFileName(resultFileName);  // указываем имя фото как имя файла
            product.addImageToProduct(image);  // добавляем image  в лист изображений продукта
        }
        if(file_five != null){
            File uploadDir = new File(uploadPath); // Превращаем ссылку на файл в объект файла
            if(!uploadDir.exists()){            // Проверяем существует ли директория
                uploadDir.mkdir();              // Если не существует - создаем
            }
            String uuidFile = UUID.randomUUID().toString();     // генерируем уникальную строку
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();  // формируем уникальное внутреннее имя файла
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));  // Сохраняем (перемещаем) файл
            Image image = new Image();  // создаем объект image
            image.setProduct(product); //  привязываем к image продукт с которым работаем
            image.setFileName(resultFileName);  // указываем имя фото как имя файла
            product.addImageToProduct(image);  // добавляем image  в лист изображений продукта
        }
        productService.saveProduct(product, category_db);
            return "redirect:/admin";
    }
}


