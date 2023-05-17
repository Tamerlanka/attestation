package com.example.springsecurityapp.controllers;

import com.example.springsecurityapp.repositories.ProductRepository;
import com.example.springsecurityapp.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Здесь пропишем логику по работе с пользователями которые не вошли в ЛК.
@Controller
@RequestMapping("/product")  // Указываем условия срабатывания контроллера (по наличию в url "/product") этот сегмент запроса нельзя повторять в методахю То есть все методы внутри данного класса имеют запросы уже после /product т.е кусок адреса RequestMapping не указвается в запросах методов но учитывается. Напримеп полный запрос (указываемый в строке браузера) метода getAllProduct() это "/product" а не "" а у метода infoProduct() это "/product/info/{id}" а не "/info/{id}"
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;    //Внедряем продукт сервис (в котором прописаны все основные методы по работе с продуктом)

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }
@GetMapping("") // Ничего не указываем т.к. у нас по умолчанию  в @RequestMapping  стоит "/product"
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct()); // ключ "product" значение: лист всех продуктов
        return "/product/product";
    }

    // Метод отрабатывает нажатие на ссылку в каталоге товара product неавторизованным пользователем (информация о товаре)
    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){ // с помощью @PathVariable получаем id
        model.addAttribute("product", productService.getProductId(id)); // Кладем в модель продукт id
        return "/product/infoProduct";      // возвращаем шаблон
    }

    // required = false, - данный атрибут может не иметь значений
    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "")String price, @RequestParam(value = "contract", required = false, defaultValue = "")String contract, Model model) {

        model.addAttribute("products", productService.getAllProduct()); // кладем в модель список всех продуктов


        if (!ot.isEmpty() & !Do.isEmpty()) {    // Если input (ot) и (do) непустые

            if (!price.isEmpty()) {       // Если radio input "price" не пустые

                if (price.equals("sorted_by_ascending_price")) { // Если содержимое radio input "price" это "sorted_by_ascending_price"

                    if (!contract.isEmpty()) {      // Если содержимое radio input "contract" не пустое
                        System.out.println(contract);
                        if (contract.equals("furniture")) { // Если содержимое radio input "contract" это "furniture"
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1)); // Вызываем метод в аргументах передаем параметры поиска
                        } else if (contract.equals("appliances")) { // Если содержимое radio input "contract" это "appliances"
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3)); // Вызываем метод в аргументах передаем параметры поиска
                        } else if (contract.equals("clothes")) {    // Если содержимое radio input "contract" это "clother"
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2)); // Вызываем метод в аргументах передаем параметры поиска
                        }
                    } else {    // Иначе, если содержимое radio input "contract" пустое (категория пуста)
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));   // Вызываем метод в аргументах передаем параметры поиска без указания категории т.к. она не указана
                    }
                } else if (price.equals("sorted_by_descending_price")) { // Если содержимое radio input "price" это "sorted_by_ascending_price"
                    if (!contract.isEmpty()) {  // Если содержимое radio input "contract" не пустое
                        if (contract.equals("furniture")) { // Если содержимое radio input "contract" это "furniture"
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1)); // Вызываем метод в аргументах передаем параметры поиска
                        } else if (contract.equals("appliances")) { // Если содержимое radio input "contract" это "appliances"
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3)); // Вызываем метод в аргументах передаем параметры поиска
                        } else if (contract.equals("clothes")) {  // Если содержимое radio input "contract" это "clother"
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2)); // Вызываем метод в аргументах передаем параметры поиска
                        }
                    } else {    // Иначе, если содержимое radio input "contract" пустое (категория пуста)
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));   // Вызываем метод в аргументах передаем параметры поиска без указания категории т.к. она не указана
                    }
                }
        } else {    // Если radio input "price" пустые

            model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do))); // Вызываем метод в аргументах передаем параметры поиска
        }
    } else { // Если input (ot) и (do) пустые
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search.toLowerCase())); // Вызываем метод в аргументах передаем параметры поиска
        }

            model.addAttribute("value_search", search); // кладем обратно в модель введенные пользователем параметры поиска
            model.addAttribute("value_price_ot", ot);// кладем обратно в модель введенные пользователем параметры поиска
            model.addAttribute("value_price_do", Do);// кладем обратно в модель введенные пользователем параметры поиска
        return "/product/product";
    }
}
