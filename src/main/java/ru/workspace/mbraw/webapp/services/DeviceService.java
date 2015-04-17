package ru.workspace.mbraw.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.workspace.mbraw.webapp.dao.IDeviceDAO;
import ru.workspace.mbraw.webapp.pojo.Device;

import java.util.List;

@Service
@Transactional
public class DeviceService {

    @Autowired
    private IDeviceDAO deviceDao;

    public List<Device> getDevicesWith(int limit, int offset) {
        return deviceDao.findWith(limit, offset);
    }

    public Device getDeviceBySerial(String serial) {
        return deviceDao.findBySerial(serial);
    }

    public void create(Device device) {
        validate(device);

        deviceDao.create(device);
    }

    public void update(Device device) {
        validate(device);

        deviceDao.update(device);
    }

    public void delete(Device device) {
        deviceDao.delete(device);
    }

    private void validate(Device device) throws IllegalArgumentException {
        if (device.getSerial() != null && device.getSerial().isEmpty()) {
            throw new IllegalArgumentException("Serial number must not be empty");
        }
    }
}
