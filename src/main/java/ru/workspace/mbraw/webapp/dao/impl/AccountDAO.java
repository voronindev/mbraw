package ru.workspace.mbraw.webapp.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.workspace.mbraw.webapp.dao.IAccountDAO;
import ru.workspace.mbraw.webapp.pojo.Account;

@Repository
public class AccountDAO extends GenericDAO<Account, Integer> implements IAccountDAO {

    @Override
    public Account findByUserName(String username) {
        return (Account) createCriteria().add(Restrictions.eq("userName", username)).uniqueResult();
    }
}
