package com.wakeUpTogetUp.togetUp.api.users.model;

import com.wakeUpTogetUp.togetUp.api.mappingGroupUser.model.MappingGroupUser;
import com.wakeUpTogetUp.togetUp.api.auth.LoginType;
import com.wakeUpTogetUp.togetUp.api.users.fcmToken.FcmToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name="user")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Integer id = null;

    @Column(name = "name")
    private String name;

    @Column(name = "social_id")
    private String socialId;


    @Column(name = "password")
    private String password;

    @Column(name = "login_type", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private LoginType loginType ;

    @Column(name = "agree_push")
    private boolean agreePush;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;


    @OneToMany(mappedBy = "user")
    @Column(name = "fcm_token")
    private List<FcmToken> fcmToken = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    private List<MappingGroupUser> mappingGroupUsers = new ArrayList<>();

    @PrePersist
    void registeredAt() {
        this.createdAt = Timestamp.from(Instant.now());

    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreDestroy
    void deletedAt() {
        this.deletedAt = Timestamp.from(Instant.now());
    }


    @Builder
    public User(Integer id, String socialId,String password, String name, LoginType loginType) {
        this.id = id;
        this.socialId=socialId;
        this.password=password;
        this.name = name;
        this.loginType=loginType;
    }
}
