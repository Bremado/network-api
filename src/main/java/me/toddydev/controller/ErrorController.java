package me.toddydev.controller;

import me.toddydev.utils.RequestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static me.toddydev.utils.RequestResponse.Action.PATH_NOT_FOUND;

@RestController("/")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/")
    @ResponseBody
    public String index() throws IOException {
        return new RequestResponse("OK", PATH_NOT_FOUND, "Welcome a BRPayments API, developed by Bremado.").build();
    }
    @GetMapping("/error")
    @ResponseBody
    public String handleError() throws IOException {
        return new RequestResponse("OK", PATH_NOT_FOUND, "This path doesn't exists.").build();
    }
}
