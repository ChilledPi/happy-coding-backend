package com.example.demo.service;

import com.example.demo.entity.Diary;
import com.example.demo.entity.Reaction;
import com.example.demo.entity.Users;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.ReactionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReactionServiceImpl implements IReactionService {


    private final UserRepository userRepository;

    private final DiaryRepository diaryRepository;

    public ReactionServiceImpl(UserRepository userRepository, DiaryRepository diaryRepository, ReactionRepository reactionRepository) {
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
    }

    @Override
    public void toggleDiaryLike(Long userId, Long diaryId) {
        Users users = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(diaryId).get();

        List<Reaction> reactions = diary.getReactions();

        Optional<Reaction> optionalReaction = reactions.stream().filter(reaction -> Objects.equals(reaction.getUser().getId(), userId)).findAny();

        if (optionalReaction.isEmpty()) {
            reactions.add(Reaction.createReaction(users, diary));
        } else {
            diary.subtractLikeCount();
            reactions.removeIf(reaction -> Objects.equals(userId, reaction.getUser().getId()));
        }
    }
}
