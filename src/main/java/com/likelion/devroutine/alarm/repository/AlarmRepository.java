package com.likelion.devroutine.alarm.repository;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
    List<Alarm> findByUserId(Long userId);

    List<Alarm> findByUserIdAndAlarmType(Long userId, AlarmType alarmType);

    void deleteByTargetIdAndFromUserId(Long targetId, Long fromUserId);
}
