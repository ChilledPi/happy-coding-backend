package com.example.demo.repository;

import com.example.demo.entity.Diary;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.DiaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findByUser(Users users, Pageable pageable);

    Page<Diary> findByUserNotAndDiaryStatus(Users users, DiaryStatus diaryStatus, Pageable pageable);

//    @Query(
//            "SELECT d FROM Diary d WHERE d.diaryStatus >= :diaryStatus " +
//                    "AND d.user.id IN " +
//                    "(SELECT f.follower.id FROM Following f WHERE f.follow.id = :userId)"
//    )
    //Page<Diary> findByFollowingUserAndDiaryStatusGreaterThanEqual(@Param("userId") Long userId, @Param("diaryStatus") DiaryStatus diaryStatus, Pageable pageable);

    Page<Diary> findByUserIdInAndStatusIn(List<Long> userIds, List<DiaryStatus> statuses, Pageable pageable);
}
