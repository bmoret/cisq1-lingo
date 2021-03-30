package nl.hu.cisq1.lingo.trainer.presentation;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Map<String, String>> nfHandler(NotFoundException NF) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  NF.getMessage());

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> iaHandler(IllegalArgumentException ia) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Error",  ia.getMessage());

        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

}
