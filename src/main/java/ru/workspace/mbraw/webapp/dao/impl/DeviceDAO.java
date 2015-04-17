package ru.workspace.mbraw.webapp.dao.impl;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.workspace.mbraw.webapp.dao.IDeviceDAO;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;

import java.util.List;

@Repository
public class DeviceDAO extends GenericDAO<Device, Integer> implements IDeviceDAO {

    @Override
    public Device findBySerial(String serial) {
        return (Device) createCriteria().add(Restrictions.eq("serial", serial).ignoreCase()).uniqueResult();
    }

    @Override
    public List<Device> findByPlatformWith(Platform platform, int limit, int offset) {
        return createCriteria().add(Restrictions.eq("platform", platform))
                .setFirstResult(offset).setMaxResults(limit).list();
    }

    @Override
    public void unbindPlatform(Platform platform) {
        String queryString = "update Device d set d.platform=null where d.platform=:platform";
        Query query = getSession().createQuery(queryString);
        query.setEntity("platform", platform);
        query.executeUpdate();
    }

    @Override
    public void deleteByPlatform(Platform platform) {
        String queryString = "delete Device d where d.platform=:platform";
        Query query = getSession().createQuery(queryString);
        query.setEntity("platform", platform);
        query.executeUpdate();
    }
}
