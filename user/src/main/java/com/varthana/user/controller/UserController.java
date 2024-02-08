package com.varthana.user.controller;

import com.varthana.user.dto.*;
import com.varthana.user.entity.User;
import com.varthana.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String home(Model model){
        try {
            String url = "http://localhost:8080/getAllBooks";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET,entity,List.class);
            List<BookDetailDto> bookDetailsDto = response.getBody();
            model.addAttribute("books", bookDetailsDto);
            return "home";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/request-rent-book")
    public String rentDetails(Model model,
                              @RequestParam("id") int id,
                              @RequestParam("name") String name){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/check-user-rented-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            UserBookDto userBookDto = new UserBookDto(id,user.getId());
            HttpEntity<Object> entity = new HttpEntity<>(userBookDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isBookRentedByUser = response.getBody();

            if(isBookRentedByUser){
                return "already-rented";
            }
            else{
                model.addAttribute("id", id);
                model.addAttribute("name", name);
                return "rent-details";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/rent-book")
    public String rentBook(Model model,
                           @RequestParam("id") int id,
                           @RequestParam("startdate") LocalDate startDate,
                           @RequestParam("enddate") LocalDate endDate){
        try {
            long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
            System.out.println(daysDifference+" "+"/rent-book  in  UserController");
            if (daysDifference <= 0) {
                return "error";
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            BookRentRequestDto bookRentRequestDto = new BookRentRequestDto(id,user.getId(),startDate,endDate);

            String url = "http://localhost:8080/rent-book" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookRentRequestDto,httpHeaders);
            ResponseEntity<BookDetailDto> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,BookDetailDto.class);
            BookDetailDto bookDetailDto = response.getBody();

            System.out.println(bookDetailDto+" "+"/rent-book  in  UserController");

            if(bookDetailDto==null){
                return "error";
            }

            return "redirect:/";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/rented-books")
    public String getRentedBooks(Model model){//id
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/get-rented-books/"+user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

//            UserIdDto userIdDto = new UserIdDto(user.getId());
//            System.out.println(userIdDto.getUserId());
            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
//            System.out.println("Before");
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET,entity,List.class);
//            System.out.println("After");
            List<RentedBooksDto> rentedBooksDtos = response.getBody();
            model.addAttribute("books", rentedBooksDtos);
            return "rented-books";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/purchase-book")
    public String purchase(Model model, @RequestParam("id") int id){
        try {
            String url = "http://localhost:8080/getAllBooks";
            ResponseEntity<List> response
                    = restTemplate.getForEntity(url, List.class);
            List<BookDetailDto> bookDetailsDto = response.getBody();
            model.addAttribute("books", bookDetailsDto);
            return "home";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/return-book")
    public String returnBook(Model model,
                             @RequestParam("id") int id,
                             @RequestParam("transactionId") UUID transactionId){
        try {
            String url = "http://localhost:8080/return-book";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            ReturnBookDto returnBookDto = new ReturnBookDto(id,transactionId);
            HttpEntity<Object> entity = new HttpEntity<>(returnBookDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isAbleToReturnBook = response.getBody();
            if(!isAbleToReturnBook){
                return "error";
            }
            return "redirect:/";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/cart-quantity")
    public String cartBook(Model model,
                             @RequestParam("id") int id,
                             @RequestParam("name") String name){
        try {
            model.addAttribute("id",id);
            model.addAttribute("name",name);
            return "cart-quantity";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/add-to-cart")
    public String addToCart(Model model,
                           @RequestParam("id") int id,
                            @RequestParam("quantity") long quantity){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            BookCartQuantityDto bookCartQuantityDto = new BookCartQuantityDto(id,user.getId(),quantity);

            String url = "http://localhost:8080/add-to-cart" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookCartQuantityDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isAddedToCart = response.getBody();
            if(!isAddedToCart){
                return "not-added-to-cart";
            }
            return "redirect:/view-cart";
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/view-cart")
    public String viewCart(Model model){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            String url = "http://localhost:8080/get-cart-books/"+user.getId();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List> response
                    = restTemplate.exchange(url, HttpMethod.GET,entity,List.class);
            List<CartDto> rentedBooksDtos = response.getBody();
            model.addAttribute("books", rentedBooksDtos);
            return "cart";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(Model model,
                                 @RequestParam("id") int bookId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            BookCartQuantityDto bookCartQuantityDto = new BookCartQuantityDto(bookId,user.getId(),0);

            String url = "http://localhost:8080/remove-from-cart" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookCartQuantityDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isDeleted = response.getBody();
            if(isDeleted){
                return "redirect:/view-cart";
            }
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/change-quantity")
    public String changeQuantity(Model model,
                                 @RequestParam("id") int bookId,
                                 @RequestParam("name") String name,
                                 @RequestParam("quantity") long quantity){
        try{
            model.addAttribute("bookId",bookId);
            model.addAttribute("name",name);
            model.addAttribute("quantity",quantity);
            System.out.println(bookId+" "+name+" "+quantity);
            return "change-quantity";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(Model model,
                                 @RequestParam("bookId") int bookId,
                                 @RequestParam("quantity") long quantity){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userService.getUserByEmail(authentication.getName());

            System.out.println("Updating quantity");

            BookCartQuantityDto bookCartQuantityDto = new BookCartQuantityDto(bookId,user.getId(),quantity);

            String url = "http://localhost:8080/add-to-cart" ;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("kamalgoudkatta@gmail.com","123");

            HttpEntity<Object> entity = new HttpEntity<>(bookCartQuantityDto,httpHeaders);
            ResponseEntity<Boolean> response
                    = restTemplate.exchange(url, HttpMethod.POST,entity,Boolean.class);
            Boolean isAddedToCart = response.getBody();
            if(!isAddedToCart){
                return "not-added-to-cart";
            }
            return "redirect:/view-cart";
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}


//@RestController
//@RequestMapping("/endpoint1")
//public class Endpoint1Controller {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping("/send-data")
//    public ResponseEntity<String> sendDataToEndpoint2() {
//        // Create DTO object to send data
//        DataDTO dataDTO = new DataDTO("Hello from Endpoint 1");
//
//        // Make POST request to Endpoint 2
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
//                "http://localhost:8080/endpoint2/receive-data",
//                dataDTO,
//                String.class);
//
//        return ResponseEntity.ok(responseEntity.getBody());
//    }
//}


//@RestController
//@RequestMapping("/endpoint2")
//public class Endpoint2Controller {
//
//    @PostMapping("/receive-data")
//    public ResponseEntity<String> receiveDataFromEndpoint1(@RequestBody DataDTO dataDTO) {
//        // Process data received from Endpoint 1
//        String message = "Data received: " + dataDTO.getMessage();
//        return ResponseEntity.ok(message);
//    }
//}


// @RestController
//@RequestMapping("/endpoint1")
//public class Endpoint1Controller {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping("/send-data")
//    public ResponseEntity<List<String>> sendDataToEndpoint2() {
//        // Create a list of strings to send
//        List<String> dataList = Arrays.asList("Data 1", "Data 2", "Data 3");
//
//        // Make POST request to Endpoint 2
//        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
//                "http://localhost:8080/endpoint2/receive-data",
//                HttpMethod.POST,
//                new HttpEntity<>(dataList),
//                new ParameterizedTypeReference<List<String>>() {});
//
//        return ResponseEntity.ok(responseEntity.getBody());
//    }
//}


// @RestController
//@RequestMapping("/endpoint2")
//public class Endpoint2Controller {
//
//    @PostMapping("/receive-data")
//    public ResponseEntity<List<String>> receiveDataFromEndpoint1(@RequestBody List<String> dataList) {
//        // Process list of strings received from Endpoint 1
//        dataList.forEach(System.out::println); // Example: printing the received data
//
//        // You can process the received list and return a response if needed
//        List<String> responseData = Arrays.asList("Response 1", "Response 2", "Response 3");
//        return ResponseEntity.ok(responseData);
//    }
//}


//public class Main {
//    public static void main(String[] args) {
//        // Example LocalDateTime objects
//        LocalDateTime dateTime1 = LocalDateTime.of(2024, 2, 6, 10, 0);
//        LocalDateTime dateTime2 = LocalDateTime.of(2024, 2, 10, 15, 30);
//
//        // Convert LocalDateTime to LocalDate
//        LocalDate date1 = dateTime1.toLocalDate();
//        LocalDate date2 = dateTime2.toLocalDate();
//
//        // Calculate the difference between the two LocalDates
//        long daysDifference = ChronoUnit.DAYS.between(date1, date2);
//
//        System.out.println("Difference in days: " + daysDifference);
//    }
//}