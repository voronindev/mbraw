package ru.workspace.mbraw.webapp.pojo;

import javax.persistence.*;

@Entity
@Table(name = "device")
public class Device {
    private Integer id;
    private String serial;
    private String address;
    private String description;
    private Platform platform;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(unique = true, length = 10)
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return (id != null ? !id.equals(device.id) : device.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Device").append(" [id: ").append(id);
        sb.append(", serial: ").append(serial);
        sb.append(", address: ").append(address);
        sb.append(", description: ").append(description);
        sb.append("]");
        return sb.toString();
    }
}
