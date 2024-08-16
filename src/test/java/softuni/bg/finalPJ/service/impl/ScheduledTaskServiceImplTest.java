package softuni.bg.finalPJ.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduledTaskServiceImplTest {

    private ScheduledTaskServiceImpl serviceToTest;

    @BeforeEach
    void setUp() {
        serviceToTest = Mockito.spy(new ScheduledTaskServiceImpl());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateWeeklyMessage() {
        // Given
        LocalDateTime fixedNow = LocalDateTime.of(2024, 8, 16, 10, 0, 0);
        String expectedFormattedDate = "08/16/2024 10:00:00";
        String expectedMessage = "The date is " + expectedFormattedDate + " is your information up to date?";

        // Mock the formatDate method to return the expected formatted date
        Mockito.doReturn(expectedFormattedDate).when(serviceToTest).formatDate(Mockito.any(LocalDateTime.class));

        // When
        serviceToTest.generateWeeklyMessage();

        // Then
        String actualMessage = serviceToTest.getWeeklyMessage();
        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetWeeklyMessage() {
        // Given
        String testMessage = "Test message";
        serviceToTest.generateWeeklyMessage();

        // When
        String result = serviceToTest.getWeeklyMessage();

        // Then
        assertNotNull(result);
    }

    @Test
    void testFormatDate() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2024, 8, 16, 10, 0, 0);
        String expectedFormattedDate = "08/16/2024 10:00:00";

        // When
        String formattedDate = serviceToTest.formatDate(dateTime);

        // Then
        assertEquals(expectedFormattedDate, formattedDate);
    }
}
