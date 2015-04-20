package ru.workspace.mbraw.webapp.dao;

import ru.workspace.mbraw.webapp.pojo.Account;

public interface IAccountDAO extends IGenericDAO<Account, Integer> {

    Account findByUserName(String username);

}
