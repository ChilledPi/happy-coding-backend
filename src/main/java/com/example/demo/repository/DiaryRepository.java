package com.example.demo.repository;

import com.example.demo.entity.Diary;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.DiaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findByDiaryStatus(DiaryStatus diaryStatus, Pageable pageable);
    Page<Diary> findByUserAndDiaryStatusLessThanEqual(Users user, DiaryStatus diaryStatus, Pageable pageable);
}
