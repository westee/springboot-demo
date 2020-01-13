package hello.entity;

import java.time.Instant;

public class User {
    Integer id;
    String username;
    String encryptedPassword;
    String avatar;
    Instant createdAt;
    Instant updatedAt;

//    public User(Integer id, String name) {
//        this.id = id;
//        this.username = name;
//    }

    public User(Integer id, String name, String encryptedPassword) {
        this.id = id;
        this.username = name;
        this.encryptedPassword = encryptedPassword;
        this.avatar = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() { return avatar; }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
