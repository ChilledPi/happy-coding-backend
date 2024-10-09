package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import com.example.demo.entity.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String type;
    private Long size;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id")
    private Users user = null;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "diary_id")
    private Diary diary = null;

    public void setUser(Users user){
        this.user = user;
    }

    public void setDiary(Diary diary){
        this.diary = diary;
    }
    public static Image createImage(String name, String url, String type, Long size, ImageType imageType, Users user, Diary diary) {
        return  Image.builder()
                .name(name)
                .url(url)
                .type(type)
                .size(size)
                .imageType(imageType)
                .build();
    }




}
