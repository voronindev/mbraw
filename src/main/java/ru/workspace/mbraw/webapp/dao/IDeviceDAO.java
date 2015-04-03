package ru.workspace.mbraw.webapp.dao;

import ru.workspace.mbraw.webapp.pojo.Device;

public interface IDeviceDAO extends IGenericDAO<Device, Integer> {

    Device findBySerial(String serial);

}
