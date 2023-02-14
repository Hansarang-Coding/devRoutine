package com.likelion.devroutine.alarm.controller;

import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.alarm.service.AlarmService;
import com.likelion.devroutine.invite.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alarms")
@Slf4j
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;
    private final InviteService inviteService;

    @GetMapping
    public String alarmList(Model model, Authentication authentication){

        return "alarm/list";
    }

    @GetMapping("/likes")
    public String likeAlarmList(Model model, Authentication authentication){
        model.addAttribute("alarmType", "like");
        model.addAttribute("likeAlarms", alarmService.findLikeAlarm(authentication.getName()));
        return "alarm/list";
    }

    @GetMapping("/comments")
    public String commentAlarmList(Model model, Authentication authentication){
        model.addAttribute("alarmType", "comment");
        model.addAttribute("commentAlarms", alarmService.findCommentAlarm(authentication.getName()));
        return "alarm/list";
    }

    @GetMapping("/follows")
    public String followAlarmList(Model model, Authentication authentication){
        model.addAttribute("alarmType", "follow");
        model.addAttribute("followAlarms", alarmService.findFollowAlarm(authentication.getName()));
        return "alarm/list";
    }

    @GetMapping("/invites")
    public String inviteAlarmList(Model model, Authentication authentication){
        model.addAttribute("alarmType", "invite");
        model.addAttribute("inviteAlarms", inviteService.findAllInvite(authentication.getName()));
        return "alarm/list";
    }
}
