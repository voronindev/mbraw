package ru.workspace.mbraw.webapp.controllers;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.workspace.mbraw.webapp.dto.DeviceDto;
import ru.workspace.mbraw.webapp.dto.PlatformDto;
import ru.workspace.mbraw.webapp.exceptions.DeviceBindException;
import ru.workspace.mbraw.webapp.exceptions.EntityNotFoundException;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;
import ru.workspace.mbraw.webapp.properties.CommonProperties;
import ru.workspace.mbraw.webapp.services.DeviceService;
import ru.workspace.mbraw.webapp.services.PlatformService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/platforms",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MapperFacade mapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<PlatformDto> listWith(@RequestParam(defaultValue = CommonProperties.DEFAULT_FIND_LIMIT) int limit,
                                      @RequestParam(defaultValue = "0") int offset) {
        List<Platform> list = platformService.getPlatformsWith(limit, offset);

        return mapper.mapAsList(list, PlatformDto.class);
    }

    @RequestMapping(value = "/{platformId}/devices", method = RequestMethod.GET)
    public List<DeviceDto> devices(@RequestParam(defaultValue = CommonProperties.DEFAULT_FIND_LIMIT) int limit,
                                   @RequestParam(defaultValue = "0") int offset,
                                   @PathVariable Integer platformId) throws EntityNotFoundException {
        Platform platform = platformService.getPlatformById(platformId);
        if (platform == null) {
            throw new EntityNotFoundException();
        }

        List<Device> devices = platformService.getDevicesByPlatformWith(platform, limit, offset);

        return mapper.mapAsList(devices, DeviceDto.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public PlatformDto add(@RequestBody PlatformDto dto) {
        Platform platform = mapper.map(dto, Platform.class);
        platformService.create(platform);

        return mapper.map(platform, PlatformDto.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void edit(@PathVariable Integer id, @RequestBody PlatformDto dto) throws EntityNotFoundException {
        Platform platform = platformService.getPlatformById(id);
        if (platform == null) {
            throw new EntityNotFoundException();
        }
        platformService.update(mapper.map(dto, Platform.class));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) throws EntityNotFoundException {
        Platform platform = platformService.getPlatformById(id);
        if (platform == null) {
            throw new EntityNotFoundException();
        }
        platformService.delete(platform);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PlatformDto platformById(@PathVariable Integer id) throws EntityNotFoundException {
        Platform platform = platformService.getPlatformById(id);
        if (platform == null) {
            throw new EntityNotFoundException();
        }
        return mapper.map(platform, PlatformDto.class);
    }

    @RequestMapping(value = "/devices/{serial}", method = RequestMethod.GET)
    public PlatformDto platformByDeviceSerial(@PathVariable String serial) throws EntityNotFoundException, DeviceBindException {
        Device device = deviceService.getDeviceBySerial(serial);
        if (device == null) {
            throw new EntityNotFoundException("Couldn't find such device");
        }
        Platform platform = device.getPlatform();
        if (platform == null) {
            throw new DeviceBindException("Device is not bind to platform");
        }
        return mapper.map(platform, PlatformDto.class);
    }
}
