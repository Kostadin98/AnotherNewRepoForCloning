package softuni.bg.finalPJ.service;

import java.time.LocalDateTime;

public interface ScheduledTaskService {

    String getWeeklyMessage();

    void generateWeeklyMessage();

    String formatDate(LocalDateTime localDateTime);

}
