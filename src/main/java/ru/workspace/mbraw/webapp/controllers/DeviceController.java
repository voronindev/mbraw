package ru.workspace.mbraw.webapp.controllers;


import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.workspace.mbraw.webapp.dto.DeviceDto;
import ru.workspace.mbraw.webapp.exceptions.DeviceBindException;
import ru.workspace.mbraw.webapp.exceptions.EntityNotFoundException;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;
import ru.workspace.mbraw.webapp.properties.CommonProperties;
import ru.workspace.mbraw.webapp.services.DeviceService;
import ru.workspace.mbraw.webapp.services.PlatformService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/devices",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private MapperFacade mapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<DeviceDto> listWith(@RequestParam(defaultValue = CommonProperties.DEFAULT_FIND_LIMIT) int limit,
                                    @RequestParam(defaultValue = "0") int offset) {
        List<Device> list = deviceService.getDevicesWith(limit, offset);

        return mapper.mapAsList(list, DeviceDto.class);
    }


    @RequestMapping(value = "/{serial}", method = RequestMethod.GET)
    public DeviceDto deviceById(@PathVariable String serial) throws EntityNotFoundException {
        Device device = deviceService.getDeviceBySerial(serial);
        if (device == null) {
            throw new EntityNotFoundException();
        }
        return mapper.map(device, DeviceDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public DeviceDto add(@RequestBody DeviceDto dto) {
        Device device = mapper.map(dto, Device.class);
        deviceService.create(device);

        return mapper.map(device, DeviceDto.class);
    }

    @RequestMapping(value = "/{serial}", method = RequestMethod.PUT)
    public void edit(@PathVariable String serial, @RequestBody DeviceDto dto) throws EntityNotFoundException {
        Device device = deviceService.getDeviceBySerial(serial);
        if (device == null) {
            throw new EntityNotFoundException();
        }
        deviceService.update(mapper.map(dto, Device.class));
    }

    @RequestMapping(value = "/{serial}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String serial) throws EntityNotFoundException {
        Device device = deviceService.getDeviceBySerial(serial);
        if (device == null) {
            throw new EntityNotFoundException();
        }
        deviceService.delete(device);
    }

    @RequestMapping(value = "/{serial}/platforms/{platformId}", method = RequestMethod.POST)
    public void bindDeviceToPlatform(@PathVariable String serial,
                                     @PathVariable Integer platformId) throws EntityNotFoundException, DeviceBindException {
        Device device = deviceService.getDeviceBySerial(serial);
        if (device == null) {
            throw new EntityNotFoundException("Couldn't find such device");
        }
        if (device.getPlatform() != null) {
            throw new DeviceBindException("Device is bind to platform already");
        }
        Platform platform = platformService.getPlatformById(platformId);
        if (platform == null) {
            throw new EntityNotFoundException("Couldn't find such platform");
        }

        device.setPlatform(platform);
        deviceService.update(device);
    }

    @RequestMapping(value = "/{serial}/platforms/{platformId}", method = RequestMethod.DELETE)
    public void unbindDeviceFromPlatform(@PathVariable String serial,
                                         @PathVariable Integer platformId) throws EntityNotFoundException {
        Device device = deviceService.getDeviceBySerial(serial);
        if (device == null) {
            throw new EntityNotFoundException("Couldn't find such device");
        }
        Platform platform = platformService.getPlatformById(platformId);
        if (platform == null) {
            throw new EntityNotFoundException("Couldn't find such platform");
        }

        device.setPlatform(null);
        deviceService.update(device);
    }
}
