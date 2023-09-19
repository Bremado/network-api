package me.toddydev.controller.v1.accounts;

import me.toddydev.Init;
import me.toddydev.model.accounts.Account;
import me.toddydev.repository.AccountRepository;
import me.toddydev.utils.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository repo;

    @RequestMapping(value = "/v1/accounts/{uniqueId}", method = RequestMethod.GET)
    public String findAccountByUniqueId(@PathVariable String uniqueId) {
        Account a = repo.findById(uniqueId).orElse(null);
        if (a == null) {
            return new RequestResponse("ERROR", RequestResponse.Action.ACCOUNT_NOT_FOUND, "This account doesn't exists.").build();
        }
        return new RequestResponse("SUCCESS", RequestResponse.Action.ACCOUNT_FOUNDED, Init.getGson().toJson(a)).build();
    }

    @RequestMapping(value = "/v1/accounts/create", method = RequestMethod.POST)
    public String createAccount(@RequestBody String body) {
        try {
            Account a = Init.getGson().fromJson(body, Account.class);
            if (repo.findById(a.getUniqueId()).orElse(null) != null) {
                return new RequestResponse("ERROR", RequestResponse.Action.ACCOUNT_ALREADY_EXISTS, "This account already exists.").build();
            }
            repo.save(a);
            return new RequestResponse("SUCCESS", RequestResponse.Action.ACCOUNT_CREATED, "Account created successfully.").build();
        } catch (Exception e) {
                return new RequestResponse("ERROR", RequestResponse.Action.ERROR_OCCURRED, e.getLocalizedMessage()).build();
        }
    }
}
