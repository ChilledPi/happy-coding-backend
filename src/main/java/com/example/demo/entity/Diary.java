package com.example.demo.entity;

import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.entity.base.BaseEntity;
import com.example.demo.entity.enums.DiaryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @Builder.Default
    private LocalDate date = LocalDate.now();
    @Builder.Default
    private Integer likesCount = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DiaryStatus diaryStatus = PUBLIC;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    public static Diary createDiary(double latitude, double longitude, String title, String content){
        return Diary.builder()
                .latitude(latitude)
                .longitude(longitude)
                .title(title)
                .content(content)
                .build();
    }

    public void updateDiary(DiaryRequestDto diaryRequestDto) {
        latitude = diaryRequestDto.getLatitude();
        longitude = diaryRequestDto.getLongitude();
        title = diaryRequestDto.getTitle();
        content = diaryRequestDto.getContent();
        date = diaryRequestDto.getDate();
        diaryStatus = diaryRequestDto.getDiaryStatus();
    }

    public void addLikesCount(){
        this.likesCount++;
    }

    public void subtractLikeCount() {
        this.likesCount--;
    }


    public void addImage(Image image){
        image.setDiary(this);
        images.add(image);
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
