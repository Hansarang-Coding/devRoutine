package com.likelion.devroutine.alarm.repository;

import com.likelion.devroutine.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
