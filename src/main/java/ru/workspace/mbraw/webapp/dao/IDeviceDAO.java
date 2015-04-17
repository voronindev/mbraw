package ru.workspace.mbraw.webapp.dao;

import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;

import java.util.List;

public interface IDeviceDAO extends IGenericDAO<Device, Integer> {

    Device findBySerial(String serial);

    List<Device> findByPlatformWith(Platform platform, int limit, int offset);

    void unbindPlatform(Platform platform);

    void deleteByPlatform(Platform platform);
}
