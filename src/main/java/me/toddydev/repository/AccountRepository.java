package me.toddydev.repository;

import me.toddydev.model.accounts.Account;
import me.toddydev.model.payments.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> { }
