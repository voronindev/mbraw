package ru.workspace.mbraw.webapp;

import org.springframework.context.annotation.Profile;
import ru.workspace.mbraw.webapp.pojo.Device;
import ru.workspace.mbraw.webapp.pojo.Platform;

import java.util.Random;

@Profile("test")
public class EntityGenerator {

    private static Random randomValue = new Random(System.currentTimeMillis());

    public static Device getRandomDevice() {
        int max = 99999999;
        int min = 10000000;
        int value = randomValue.nextInt(max - min + 1) + min;

        String serial = "vd" + String.valueOf(value);

        Device device = new Device();
        device.setSerial(serial);
        device.setAddress("address");
        device.setDescription("description");

        return device;
    }

    public static Platform getRandomPlatform() {
        int max = 9999999;
        int min = 1000000;
        int value = randomValue.nextInt(max - min + 1) + min;

        Platform platform = new Platform();
        platform.setAddress("New platform, index: " + value);
        platform.setDescription("Uses: " + value);
        platform.setCapacity(30);

        return platform;
    }
}
