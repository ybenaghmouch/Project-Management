package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class CronJobs {
    private final UserRepository userRepository;



    @Scheduled(cron = "0 0 0 24 * ?")
    @Transactional
    public void updateSoldCongeMonthly() {
        System.out.println("Updated sold conge");
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            double newSoldConge = user.getSoldeConge() +1.5;
            user.setSoldeConge(newSoldConge);
        });

        userRepository.saveAll(users);


    }


    @Scheduled(cron = "0 0 0 1 1 ?")
    @Transactional
    public void updateSoldCongeYearly() {
        System.out.println("Updated sold conge");
        List<User> users = userRepository.findAll();
        users.forEach(user -> {

            user.setSoldeConge(0);
        });

        userRepository.saveAll(users);


    }
}
