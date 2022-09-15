package danil.enrollment.controllers;

import egor.enrollment.components.schemas.*;
import egor.enrollment.components.schemas.Error;
import egor.enrollment.exception.BadRequestException;
import egor.enrollment.exception.NotFoundException;
import egor.enrollment.model.Item;
import egor.enrollment.services.ItemService;
import egor.enrollment.services.ValidationService;
import egor.enrollment.utility.ConverterItemToSystemItem;
import egor.enrollment.utility.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {
    private final ItemService service;


    @Autowired
    public ItemController(ItemService service) {
        this.service = service;

    }

    @PostMapping(value = "/imports")
    public ResponseEntity<Error> addItems(@RequestBody( ) SystemItemImportRequest request) {
        System.out.println("Пришло ???");
        ResponseEntity<Error> response;
        try {
            LocalDateTime date = Utils.getDate(request.getUpdateDate());
            System.out.println(" формат даты не верен");
        } catch (Exception e) {
             return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);
        }
        service.saveItems(request);
        response = new ResponseEntity<>(new Error(200, "Success"), HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteItems(@PathVariable String id,
                                             @RequestParam String date) {
        ResponseEntity<Error> response;
        Item item = service.findItemInDB(id);
        LocalDateTime localDateTime = null;
        try {
            localDateTime = Utils.getDate(date);
        } catch (Exception e) {
            System.out.println("dateEnd ");
            return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);
        }
        if (item != null) {
            service.deleteItemInDB(item, localDateTime);
            response = new ResponseEntity<>(new Error(200, "OK"), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(new Error(404, "Item not found"), HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<ResponseAbs> getItems(@PathVariable String id) {
        System.out.println("пришло " + id);
        if (id.isEmpty() || id.trim().isBlank()) {
            return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);
        }
        Item item = service.findItemInDB(id);
        System.out.println(item);
        if (item != null) {
            SystemItem systemItem = ConverterItemToSystemItem.toShopUnit(item);
            return new ResponseEntity<>(systemItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Error(404, "Item not found"), HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping(value = "/updates")
    public ResponseEntity<ResponseAbs> getFiles(
            @RequestParam String date
    ) {
        LocalDateTime dateTime = null;
        try {
            dateTime = service.getDate(date);
        } catch (Exception e) {
            System.out.println("catch ");
            return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);

        }

        System.out.println(date);
        SystemItemHistoryResponse response = service.getStatisticItems(dateTime);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(value = "node/{id}/history")
    public ResponseEntity<ResponseAbs> getStatistic(@PathVariable String id,
                                                    @RequestParam(required = false) Optional<String> dateStart,
                                                    @RequestParam(required = false) Optional<String> dateEnd) {
        LocalDateTime dateStartTime = null;
        LocalDateTime dateEndTime = null;
        if (dateEnd.isPresent()) {
            try {
                dateEndTime = service.getDate(dateEnd.get());
            } catch (Exception e) {
                System.out.println("dateEnd ");
                return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);

            }
        }

        if (dateStart.isPresent()) {
            try {
                dateStartTime = service.getDate(dateStart.get());
            } catch (Exception e) {
                System.out.println("dateStartTime ");
                return new ResponseEntity<>(new Error(400, "Validation Failed"), HttpStatus.BAD_REQUEST);

            }
        }
        System.out.println("Get history from: " + dateStart + " to: " + dateEnd);

        SystemItemHistoryResponse response = service.getStatisticItems(id, dateStartTime, dateEndTime);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Error(404, "Item not found"), HttpStatus.NOT_FOUND);

        }


    }

    @GetMapping(value = "test")
    public ResponseEntity<Error> getTest() {
        System.out.println(" Что пришло ! ураааа!!!!!");
        return new ResponseEntity<>(new Error(200, "Success"), HttpStatus.OK);
    }


}
