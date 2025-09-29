package com.ggiiig.retryevent.repository;

import com.ggiiig.retryevent.entity.RetryEvent;
import com.ggiiig.retryevent.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

public interface RetryEventRepository extends JpaRepository<RetryEvent, Integer> {
    List<RetryEvent> findByStatusIn(List<Status> statuses);
}
