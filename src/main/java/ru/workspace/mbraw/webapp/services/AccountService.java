package ru.workspace.mbraw.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.workspace.mbraw.webapp.dao.IAccountDAO;
import ru.workspace.mbraw.webapp.pojo.Account;

@Service
@Transactional
public class AccountService {

    @Autowired
    private IAccountDAO accountDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void create(Account account) {
        validate(account);

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        accountDao.create(account);
    }

    public void update(Account account) {
        validate(account);

        accountDao.update(account);
    }

    public void delete(Account account) {
        accountDao.delete(account);
    }

    public Account getByUserName(String username) {
        return accountDao.findByUserName(username);
    }

    private void validate(Account account) throws IllegalArgumentException {
        if (account.getUserName() != null && account.getUserName().isEmpty()) {
            throw new IllegalArgumentException("User name must not be empty");
        }
        if (account.getPassword() != null && account.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User password must not be empty");
        }
    }
}
