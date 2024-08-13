package softuni.bg.finalPJ.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.service.ScheduledTaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private String weeklyMessage;

    @Override
    public String getWeeklyMessage() {
        return weeklyMessage;
    }

    @Override
    @Scheduled(cron = "0 * * * * *") // Every Monday at 9 AM
    public void generateWeeklyMessage() {


        String formattedDate = formatDate(LocalDateTime.now());
        this.weeklyMessage = "The date is " + formattedDate + " is your information up to date?";
    }

    @Override
    public String formatDate(LocalDateTime localDateTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        String formattedDate = formatter.format(localDateTime);

        return formattedDate;
    }
}
