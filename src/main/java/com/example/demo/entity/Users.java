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

    @Builder.Default
    private Boolean premiumBadge = false;

    @Builder.Default
    private Boolean notificationsEnabled = true;

    @Builder.Default
    private Integer totalLikesCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Following> followers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "follow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Following> follows = new ArrayList<>();

    @Builder.Default
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image profileImage = null;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> likes = new ArrayList<>();


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

    public void addTotalLikesCount(){
        this.totalLikesCount++;
    }

    public void subtractTotalLikesCount() {
        this.totalLikesCount--;
    }

    public void changeProfileImage(Image image){
        image.setUser(this);
        profileImage = image;
    }

    public void addDiary(Diary diary){
        diary.setUser(this);
        diaries.add(diary);
    }

    public void addFollowing(Following following){
        following.setFollow(this);
        follows.add(following);
    }

    public void addFollower(Following following) {
        following.setFollower(this);
        followers.add(following);
    }
}
