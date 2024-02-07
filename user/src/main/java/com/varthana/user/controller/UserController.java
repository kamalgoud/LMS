package com.varthana.user.controller;

import com.varthana.user.dto.BookDetailDto;
import com.varthana.user.dto.DateTimeDto;
import com.varthana.user.dto.RentedBookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/")
    public String home(Model model){
        String url = "http://localhost:8080/getAllBooks";
        ResponseEntity<List> response
                = restTemplate.getForEntity(url, List.class);
        List<BookDetailDto> bookDetailsDto = response.getBody();
        model.addAttribute("books",bookDetailsDto);
        return "home";
    }

    @PostMapping("/request-rent-book")
    public String rentDetails(Model model,
                              @RequestParam("id") int id,
                              @RequestParam("name") String name){
        model.addAttribute("id",id);
        model.addAttribute("name",name);
        return "rent-details";
    }

    @PostMapping("/rent-book")
    public String rentBook(Model model,
                           @RequestParam("id") int id,
                           @RequestParam("startdate") LocalDate startDate,
                           @RequestParam("enddate") LocalDate endDate){
        System.out.println(startDate);
        System.out.println(endDate);
        long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        if(daysDifference<=0){
            return "error";
        }
        String url = "http://localhost:8080/rent-book/"+id;
        DateTimeDto dateTimeDto = new DateTimeDto(startDate,endDate);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, dateTimeDto, String.class);

        System.out.println(responseEntity.getBody());

//        return ResponseEntity.ok(responseEntity.getBody());

        return "redirect:/";
    }

    @PostMapping("/purchase-book")
    public String purchase(Model model, @RequestParam("id") int id){
        String url = "http://localhost:8080/getAllBooks";
        ResponseEntity<List> response
                = restTemplate.getForEntity(url, List.class);
        List<BookDetailDto> bookDetailsDto = response.getBody();
        model.addAttribute("books",bookDetailsDto);
        return "home";
    }

    @GetMapping("/rented-books")
    public String getRentedBooks(Model model){//id
        String url = "http://localhost:8080/get-rented-books";//+id
        ResponseEntity<List> response
                = restTemplate.getForEntity(url, List.class);
        List<RentedBookDto> rentedBookDetailsDto = response.getBody();
        model.addAttribute("rentedBooks",rentedBookDetailsDto);
        return "home";
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