package ru.workspace.mbraw.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.workspace.mbraw.webapp.dao.IDeviceDAO;
import ru.workspace.mbraw.webapp.dao.IPlatformDAO;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;

import java.util.List;

@Service
@Transactional
public class PlatformService {

    @Autowired
    private IPlatformDAO platformDao;

    @Autowired
    private IDeviceDAO deviceDao;

    public List<Platform> getPlatformsWith(int limit, int offset) {
        return platformDao.findWith(limit, offset);
    }

    public List<Device> getDevicesByPlatformWith(Platform platform, int limit, int offset) {
        return deviceDao.findByPlatformWith(platform, limit, offset);
    }

    public Platform getPlatformById(Integer id) {
        return platformDao.findById(id);
    }

    public void create(Platform platform) {
        validate(platform);

        platformDao.create(platform);
    }

    public void update(Platform platform) {
        validate(platform);

        platformDao.update(platform);
    }

    public void delete(Platform platform) {
        deviceDao.unbindPlatform(platform);

        platformDao.delete(platform);
    }

    private void validate(Platform platform) throws IllegalArgumentException {
        if (platform.getAddress() != null && platform.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Platform address must not be empty");
        }
        if (platform.getDescription() != null && platform.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Platform description must not be empty");
        }
        if (platform.getCapacity() != null && platform.getCapacity() < 1) {
            throw new IllegalArgumentException("Platform capacity must not be more then 1");
        }
    }
}
