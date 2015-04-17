package ru.workspace.mbraw.webapp.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.workspace.mbraw.webapp.dao.IDeviceDAO;
import ru.workspace.mbraw.webapp.pojo.Device;

@Repository
public class DeviceDAO extends GenericDAO<Device, Integer> implements IDeviceDAO {

    @Override
    public Device findBySerial(String serial) {
        return (Device) createCriteria().add(Restrictions.eq("serial", serial).ignoreCase()).uniqueResult();
    }
}
