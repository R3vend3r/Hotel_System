package hotel_system.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client implements Serializable {
    @Id
    @Column(nullable = false)
    private String id;

    @Column
    private String name;

    @Column
    private String surname;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Client() {
    }

    public Client(String id, String name, String surname) {
        setId(id);
        setName(name);
        setSurname(surname);
    }
    public Client(String name, String surname, User user) {
        this.name = name;
        this.surname = surname;
        this.user = user;
    }

    public Client(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @PrePersist
    private void generateId() {
        if (this.id == null) {
            this.id = "CL-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    public void setSurname(String surname) {
        this.surname = Objects.requireNonNull(surname, "Surname cannot be null");
        if (surname.isBlank()) {
            throw new IllegalArgumentException("Surname cannot be blank");
        }
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "Client ID cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Client ID cannot be blank");
        }
    }
    @Override
    public String toString() {
        return String.format("Client[id=%s, name=%s, surname=%s]",
                id, name, surname);
    }
}