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
import ru.workspace.mbraw.webapp.dto.PlatformDto;
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
public class PlatformControllerTests extends Assert {

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
        Platform platform = EntityGenerator.getRandomPlatform();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(platform);

        mvc.perform(post("/api/platforms").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.address", is(platform.getAddress())))
                .andExpect(jsonPath("$.description", is(platform.getDescription())))
                .andExpect(jsonPath("$.capacity", is(platform.getCapacity())));
    }

    @Test
    public void testListWith() throws Exception {
        List<Platform> platforms = new ArrayList<Platform>(10);

        for (int i = 0; i < 10; i++) {
            Platform platform = EntityGenerator.getRandomPlatform();
            platforms.add(platform);
            platformService.create(platform);
        }

        MvcResult result = mvc.perform(get("/api/platforms").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        ObjectMapper jsonMapper = new ObjectMapper();
        List<Platform> receivedList = jsonMapper.readValue(result.getResponse().getContentAsString(),
                jsonMapper.getTypeFactory().constructCollectionType(List.class, Platform.class));

        notEmpty(receivedList);
        isTrue(receivedList.containsAll(platforms));
    }

    @Test
    public void testPlatformById() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();

        platformService.create(platform);

        mvc.perform(get("/api/platforms/{id}", platform.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.address", is(platform.getAddress())))
                .andExpect(jsonPath("$.description", is(platform.getDescription())))
                .andExpect(jsonPath("$.capacity", is(platform.getCapacity())));
    }

    @Test
    public void testDeleteEntity() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();

        platformService.create(platform);

        mvc.perform(delete("/api/platforms/{id}", platform.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        isNull(platformService.getPlatformById(platform.getId()));
    }

    @Test
    public void testDeletePlatformAndUnbindDevices() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();
        platformService.create(platform);

        for (int i = 0; i < 10; i++) {
            Device device = EntityGenerator.getRandomDevice();
            device.setPlatform(platform);
            deviceService.create(device);
        }


        mvc.perform(delete("/api/platforms/{id}", platform.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        isNull(platformService.getPlatformById(platform.getId()));

        List<Device> devicesAfterDeletion = platformService.getDevicesByPlatformWith(platform, 10, 0);
        isTrue(devicesAfterDeletion.isEmpty());
    }

    @Test
    public void testUpdateEntity() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();

        platformService.create(platform);

        platform.setAddress("New address");
        platform.setDescription("New description");

        Integer id = platform.getId();
        PlatformDto dto = mapper.map(platform, PlatformDto.class);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(dto);

        mvc.perform(put("/api/platforms/{id}", id).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Platform updatedPlatform = platformService.getPlatformById(id);

        isTrue(platform.getId().equals(updatedPlatform.getId()));
        isTrue(platform.getAddress().equals(updatedPlatform.getAddress()));
        isTrue(platform.getDescription().equals(updatedPlatform.getDescription()));
    }

    @Test
    public void testPlatformByDeviceSerial() throws Exception {
        Platform platform = EntityGenerator.getRandomPlatform();
        platformService.create(platform);

        Device device = EntityGenerator.getRandomDevice();
        device.setPlatform(platform);
        deviceService.create(device);

        String serial = device.getSerial();
        mvc.perform(get("/api/platforms/devices/{serial}", serial).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.address", is(platform.getAddress())))
                .andExpect(jsonPath("$.description", is(platform.getDescription())))
                .andExpect(jsonPath("$.capacity", is(platform.getCapacity())));
    }
}
