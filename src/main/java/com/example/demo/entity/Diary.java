package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import com.example.demo.entity.enums.DiaryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.entity.enums.DiaryStatus.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Diary extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    private LocalDate date = LocalDate.now();
    private Integer likesCount = 0;

    @Enumerated(EnumType.STRING)
    private DiaryStatus diaryStatus = PUBLIC;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public static Diary createDiary(double latitude, double longitude, String title, String content){
        return Diary.builder()
                .latitude(latitude)
                .longitude(longitude)
                .title(title)
                .content(content)
                .build();
    }

    public void updateLikesCount(){
        this.likesCount++;
    }

    public void updateStatus(DiaryStatus diaryStatus){
        this.diaryStatus = diaryStatus;
    }

    public void addImage(Image image){
        image.setDiary(this);
        images.add(image);
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
