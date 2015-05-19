package ru.workspace.mbraw.webapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import ru.workspace.mbraw.webapp.ApplicationTestConfiguration;
import ru.workspace.mbraw.webapp.EntityGenerator;
import ru.workspace.mbraw.webapp.dto.DeviceDto;
import ru.workspace.mbraw.webapp.exceptions.DeviceBindException;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;
import ru.workspace.mbraw.webapp.services.DeviceService;
import ru.workspace.mbraw.webapp.services.PlatformService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTestConfiguration.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DeviceControllerTests extends Assert {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PlatformService platformService;

    private MockMvc mvc;

    @Before
    public void setUp() {
        if (mvc == null) {
            mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        }
    }

    @Test
    public void testAddEntity() throws Exception {
        Device device = EntityGenerator.getRandomDevice();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(device);

        mvc.perform(post("/api/devices").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.serial", is(device.getSerial())))
                .andExpect(jsonPath("$.address", is(device.getAddress())))
                .andExpect(jsonPath("$.description", is(device.getDescription())));
    }


    @Test
    public void testListWith() throws Exception {
        List<Device> devices = new ArrayList<Device>(15);

        for (int i = 0; i < 15; i++) {
            Device device = EntityGenerator.getRandomDevice();
            devices.add(device);
            deviceService.create(device);
        }

        MvcResult result = mvc.perform(get("/api/devices").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        ObjectMapper jsonMapper = new ObjectMapper();
        List<Device> receivedList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                jsonMapper.getTypeFactory().constructCollectionType(List.class, Device.class));

        notEmpty(receivedList);
        isTrue(receivedList.containsAll(devices));
    }

    @Test
    public void testDeviceBySerial() throws Exception {
        Device device = EntityGenerator.getRandomDevice();

        deviceService.create(device);

        String serial = device.getSerial();
        mvc.perform(get("/api/devices/{serial}", serial).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.serial", is(device.getSerial())))
                .andExpect(jsonPath("$.address", is(device.getAddress())))
                .andExpect(jsonPath("$.description", is(device.getDescription())));
    }

    @Test
    public void testDeleteEntity() throws Exception {
        Device device = EntityGenerator.getRandomDevice();

        deviceService.create(device);

        String serial = device.getSerial();
        mvc.perform(delete("/api/devices/{serial}", serial).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        isNull(deviceService.getDeviceBySerial(serial));
    }

    @Test
    public void testUpdateEntity() throws Exception {
        Device device = EntityGenerator.getRandomDevice();

        deviceService.create(device);

        device.setAddress("New address");
        device.setDescription("New description");

        String serial = device.getSerial();
        DeviceDto dto = mapper.map(device, DeviceDto.class);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(dto);

        mvc.perform(put("/api/devices/{serial}", serial).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Device updatedDevice = deviceService.getDeviceBySerial(serial);

        isTrue(device.getId().equals(updatedDevice.getId()));
        isTrue(device.getAddress().equals(updatedDevice.getAddress()));
        isTrue(device.getDescription().equals(updatedDevice.getDescription()));
    }


    @Test
    public void testListBindDevices() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();
        platformService.create(platform);

        List<Device> devicesGroup = new ArrayList<Device>(7);

        for (int i = 0; i < 7; i++) {
            Device device = EntityGenerator.getRandomDevice();
            device.setPlatform(platform);
            deviceService.create(device);
            devicesGroup.add(device);
        }

        ObjectMapper jsonMapper = new ObjectMapper();

        // Check platform 1:
        MvcResult result = mvc.perform(get("/api/platforms/{platformId}/devices", platform.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        List<Device> receivedListPlatform_1 = jsonMapper.readValue(result.getResponse().getContentAsString(),
                jsonMapper.getTypeFactory().constructCollectionType(List.class, Device.class));

        notEmpty(receivedListPlatform_1);
        isTrue(receivedListPlatform_1.containsAll(devicesGroup));
        isTrue(receivedListPlatform_1.size() == devicesGroup.size());
    }

    @Test
    public void testWrongBindDevices() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();
        platformService.create(platform);

        Platform platform2 = EntityGenerator.getRandomPlatform();
        platformService.create(platform2);

        Device device = EntityGenerator.getRandomDevice();
        device.setPlatform(platform);
        deviceService.create(device);

        MvcResult result = mvc.perform(put("/api/devices/{serial}/platforms/{platformId}", device.getSerial(), platform2.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();

        isInstanceOf(DeviceBindException.class, result.getResolvedException());
    }

    @Test
    public void testBindDevice() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();
        platformService.create(platform);

        Device device = EntityGenerator.getRandomDevice();
        deviceService.create(device);

        mvc.perform(put("/api/devices/{serial}/platforms/{platformId}", device.getSerial(), platform.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Device boundDevice = deviceService.getDeviceBySerial(device.getSerial());

        isTrue(boundDevice.getPlatform().getId().equals(platform.getId()));
    }

    @Test
    public void testUnbindDevice() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();
        platformService.create(platform);

        Device device = EntityGenerator.getRandomDevice();
        device.setPlatform(platform);
        deviceService.create(device);

        mvc.perform(delete("/api/devices/{serial}/platforms/{platformId}", device.getSerial(), platform.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Device boundDevice = deviceService.getDeviceBySerial(device.getSerial());

        isNull(boundDevice.getPlatform());
    }
}
