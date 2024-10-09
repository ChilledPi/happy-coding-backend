package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private Boolean premiumBadge = false;
    private Boolean notificationsEnabled = true;

    private Integer totalLikesCount = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Following> followings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> profileImages = new ArrayList<>();


    public static Users createUser(String username, String password, String name){
        return Users.builder()
                .username(username)
                .password(password)
                .name(name)
                .build();
    }

    public void getPremiumBadge(){
        this.premiumBadge = true;
    }

    public void updateNotificationEnabled(boolean status){
        this.notificationsEnabled = status;
    }

    public void updateTotalLikesCount(){
        this.totalLikesCount++;
    }

    public void addImage(Image image){
        image.setUser(this);
        profileImages.add(image);
    }

    public void addDiary(Diary diary){
        diary.setUser(this);
        diaries.add(diary);
    }

    public void addFollowing(Following following){
        following.setUser(this);
        followings.add(following);
    }



}
