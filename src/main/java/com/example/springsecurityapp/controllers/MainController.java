package com.example.springsecurityapp.controllers;

import com.example.springsecurityapp.enumm.Status;
import com.example.springsecurityapp.models.Cart;
import com.example.springsecurityapp.models.Order;
import com.example.springsecurityapp.models.Person;
import com.example.springsecurityapp.models.Product;
import com.example.springsecurityapp.repositories.CartRepository;
import com.example.springsecurityapp.repositories.OrderRepository;
import com.example.springsecurityapp.repositories.ProductRepository;
import com.example.springsecurityapp.security.PersonDetails;
import com.example.springsecurityapp.services.PersonService;
import com.example.springsecurityapp.services.ProductService;
import com.example.springsecurityapp.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private final PersonValidator personValidator;
    private final PersonService personService;

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    public MainController(PersonValidator personValidator, PersonService personService, ProductRepository productRepository, ProductService productService, CartRepository cartRepository, OrderRepository orderRepository) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.productRepository = productRepository;
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/person_accaunt")
    public String index(Model model) {
        // получаем объект аутентификации с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // получаем объект аутентификации из сессии по запросу от пользователя к сессии с которым приходят куки пользователя.
        /* Преобразуем объект аутентификации в объект пользователя */
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();  // Получаем из объекта пользователя самого пользователя а из него его роль.
        if(role.equals("ROLE_ADMIN")){  // если роль админская
            return "redirect:/admin";   // редирект на страницу admin, иначе на index
        }   // При этом админу недоступна станица index кабинета пользователя
//        System.out.println(personDetails.getPerson());
//        System.out.println("ID пользователя " + personDetails.getPerson().getId());
//        System.out.println("Логин пользователя " + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя " + personDetails.getPerson().getPassword());
//        System.out.println(personDetails);
        model.addAttribute("products", productService.getAllProduct()); // доработали метод передаем на страницу список всех товаров
        return "/user/index";
    }

    // Метод для регистрации нового пользователя 1-й способ работы с моделью аналогичен 2-му способу
//    @GetMapping("/registration")
//    public String registration(Model model){
//        model.addAttribute("person", new Person());
//        return "registration";
//    }

    /* Метод для регистрации нового пользователя 2-й способ работы с моделью аналогично 1-му способу. Метод сам обавит модель в шаблон и вернет шаблон registration */
    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) { // Spring делает все автоматически, смотрит приходит ли в запросе атрибут person
        // если нет то он его создает.
        return "registration";
    }
    // Метод принимает и обрабатывает форму регистрации. Принимает объект с модели и создает объект ошибки
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){ /* принимаем из шаблона registration
    атрибут формы (person) указываем что модель должна валидироваться и в случае наличия ошибки они будут положены в объект bindingResult */
        personValidator.validate(person, bindingResult); // через объект personValidator вызываем метод validate() из PersonValidator с атрибутами в виде объекта person из формы и объекта ошибки. Если логин из нового person будет найден в БД validate() вернет ошибку в bindingResult
        if(bindingResult.hasErrors()) {// проверяем наличие ошибки в bindingResult
            return "registration"; // Если ошибки есть возвращаем их в шаблон registration для отображения
        }
        personService.registr(person); // Если ошибки нет, если пройдены все валидации полей класса Person, то предаем объект через personService и метод registr() на регистрацию в БД.
        return "redirect:/person_accaunt"; // Если пользователь не является аутентифицированным(т.е. впервые прошел регистрацию) редирект на страницу person_accaunt будет перехвачен и пользователь попадет на страницу ввода логина и пароля, и сможет зарегистрироваться т.к. уже добавлен в БД пользователей.
    }

    // Метод отрабатывает нажатие на ссылку в каталоге товара product неавторизованным пользователем (информация о товаре)
    @GetMapping("person_accaunt/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){ // с помощью @PathVariable получаем id
        model.addAttribute("product", productService.getProductId(id)); // Кладем в модель продукт id
        return "/user/infoProduct";      // возвращаем шаблон
    }

    @PostMapping("/person_account/product/search")
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
        model.addAttribute("value_price_ot", ot); // кладем обратно в модель введенные пользователем параметры поиска
        model.addAttribute("value_price_do", Do); // кладем обратно в модель введенные пользователем параметры поиска
        return "/product/product";
    }
    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
        Product product = productService.getProductId(id); // Получаем продукт по id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Извлекаем объект аутентификации из сессии
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal(); // Из объекта аутентификации берем объект Person
        int id_person = personDetails.getPerson().getId(); // Получаем id пользователя из объекта Person
        Cart cart = new Cart(id_person, product.getId());
        cartRepository.save(cart);
        return "redirect:/cart";
    }
    @GetMapping("/cart")
    public String cart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Извлекаем объект аутентификации из сессии
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal(); // Из объекта аутентификации берем объект Person
        int id_person = personDetails.getPerson().getId(); // Получаем id пользователя из объекта Person

        List<Cart> cartList = cartRepository.findByPersonId(id_person); // Ищем по id ту корзину которая относится к данному пользователю
        List<Product> productList = new ArrayList<>(); // создаем Лист для продуктов в корзине

        for (Cart cart: cartList) { // проходим циклом все продукты cartList

            productList.add(productService.getProductId(cart.getProductId())); // Для каждого продукта из cartList через productService находим id и добавляем по этому id этот продукт в productList для отображения в корзине
        }

        float price = 0;   // Вычисляем цену товаров в корзине
        for (Product product: productList) {
            price += product.getPrice();
        }
        model.addAttribute("price", price);
        model.addAttribute("cart_product", productList);
        return "/user/cart";
    }
        @GetMapping("/cart/delete/{id}")
        public String deleteProductFromCart(Model model, @PathVariable("id") int id){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Извлекаем объект аутентификации из сессии
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal(); // Из объекта аутентификации берем объект Person
            int id_person = personDetails.getPerson().getId(); // Получаем id пользователя из объекта Person
            List<Cart> cartList = cartRepository.findByPersonId(id_person); // Ищем по id ту корзину которая относится к данному пользователю
            List<Product> productList = new ArrayList<>(); // создаем Лист для продуктов в корзине

            for (Cart cart: cartList) { // проходим циклом все продукты cartList

                productList.add(productService.getProductId(cart.getProductId())); // Для каждого продукта из cartList через productService находим id и добавляем по этому id этот продукт в productList для отображения в корзине
            }
            cartRepository.deleteCartByProductId(id);
            return "redirect:/cart";
    }
    @GetMapping("/order/create")
    public String order(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Извлекаем объект аутентификации из сессии
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal(); // Из объекта аутентификации берем объект Person
        int id_person = personDetails.getPerson().getId(); // Получаем id пользователя из объекта Person

        List<Cart> cartList = cartRepository.findByPersonId(id_person); // Ищем по id ту корзину которая относится к данному пользователю
        List<Product> productList = new ArrayList<>(); // создаем Лист для продуктов в корзине

        for (Cart cart: cartList) { // проходим циклом все продукты cartList

            productList.add(productService.getProductId(cart.getProductId())); // Для каждого продукта из cartList через productService находим id и добавляем по этому id этот продукт в productList для отображения в корзине
        }

        float price = 0;   // Вычисляем цену товаров в корзине
        for (Product product: productList) {
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString(); // генерируем уникальную строку как уникальный номер заказа
        for (Product product:productList) { // перебираем продукты в корзине
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Принят); // создаем новый заказ
            orderRepository.save(newOrder); // добавляем продукт в новый заказ
            cartRepository.deleteCartByProductId(product.getId()); // убираем добавленный в заказ продукт из корзины
        }
        return "redirect:/orders";
    }
    @GetMapping("/orders")
        public String orderUser(Model model){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
            model.addAttribute("orders", orderList);
            return "/user/orders";
        }
}
    /* Аутентификация - проверка логина и пароля. Авторизация  - проверка роли и определение доступного функционала В Spring Security роли должны начинаться с ключевого слова role_ это обязательное требование  */






















