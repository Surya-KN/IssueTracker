package com.suryakn.IssueTracker.service;

import com.suryakn.IssueTracker.dto.UserProjection;
import com.suryakn.IssueTracker.entity.UserEntity;
import com.suryakn.IssueTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<List<UserProjection>> getAllUsers() {
        List<UserEntity> userList = userRepository.findAll();
        List<UserProjection> userProjectionList = new ArrayList<>();
        for (UserEntity userEntity : userList) {
            userProjectionList.add(new UserProjection(userEntity));
        }
        return ResponseEntity.ok(userProjectionList);
    }
}
