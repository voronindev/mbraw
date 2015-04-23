package ru.workspace.mbraw.controllers.test;

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
import ru.workspace.mbraw.webapp.dto.DeviceDto;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.services.DeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private MockMvc mvc;

    private Random randomValue;

    @Before
    public void setUp() {
        if (mvc == null) {
            mvc = MockMvcBuilders.webAppContextSetup(wac).build();
            randomValue = new Random();
        }
    }

    @Test
    public void testAddEntity() throws Exception {
        Device device = getRandomDevice();
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
            Device device = getRandomDevice();
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
        Device device = getRandomDevice();

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
        Device device = getRandomDevice();

        deviceService.create(device);

        String serial = device.getSerial();
        mvc.perform(delete("/api/devices/{serial}", serial).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        isNull(deviceService.getDeviceBySerial(serial));
    }

    @Test
    public void testUpdateEntity() throws Exception {
        Device device = getRandomDevice();

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

        isTrue(device.equals(updatedDevice));
    }

    private Device getRandomDevice() {
        StringBuilder sb = new StringBuilder(10);
        sb.append("vd");

        int max = 99999999;
        int min = 10000000;

        int value = randomValue.nextInt(max - min + 1) + min;

        sb.append(String.valueOf(value));

        Device device = new Device();
        device.setSerial(sb.toString());
        device.setAddress("address");
        device.setDescription("description");

        return device;
    }
}
