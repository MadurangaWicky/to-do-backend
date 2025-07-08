package com.coveragex.todoapplication.repository;

import com.coveragex.todoapplication.entity.ToDoCard;
import com.coveragex.todoapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoCardRepository extends JpaRepository<ToDoCard, Long> {

    public Page<ToDoCard> findByUserAndIsDeletedFalseAndIsDoneFalse(User user, Pageable pageable);
    public Page<ToDoCard> findByUserAndIsDeletedFalseAndIsDoneTrue(User user, Pageable pageable);
    public Page<ToDoCard> findByUserAndIsDeletedTrue(User user, Pageable pageable);
}
