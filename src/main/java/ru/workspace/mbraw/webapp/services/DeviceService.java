package ru.workspace.mbraw.webapp.services;

import org.apache.commons.lang3.StringUtils;
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
    private IDeviceDAO dao;

    public List<Device> getDevicesWith(int limit, int offset) {
        return dao.findWith(limit, offset);
    }

    public Device getDeviceBySerial(String serial) {
        return dao.findBySerial(serial);
    }

    public void create(Device device) {
        validate(device);

        dao.create(device);
    }

    public void update(Device device) {
        validate(device);

        dao.update(device);
    }

    public void delete(Device device) {
        dao.delete(device);
    }

    private void validate(Device device) throws IllegalArgumentException {
        if (StringUtils.isBlank(device.getSerial())) {
            throw new IllegalArgumentException("Serial number must not be empty");
        }
    }
}
