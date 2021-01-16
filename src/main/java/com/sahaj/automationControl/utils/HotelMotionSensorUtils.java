package com.sahaj.automationControl.utils;

import com.sahaj.automationControl.dto.Floor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Map;

public class HotelMotionSensorUtils {

    private static final Logger logger = LoggerFactory.getLogger("HotelMotionSensorUtils");

    private static Calendar calendar = Calendar.getInstance();

    private static BigDecimal HOUR_IN_SECONDS = new BigDecimal(4600);

    public static boolean isNightTime() {
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        return (timeOfDay >= 0 && timeOfDay < 6) || (timeOfDay >= 18  && timeOfDay < 24);
    }

    public static BigDecimal getTimeDifferenceInHours(Instant previousTime, Instant currentTime) {
        BigDecimal timeElapsedInSeconds = new BigDecimal(Duration.between(previousTime, currentTime).getSeconds());
        BigDecimal timeElapsedInHours = timeElapsedInSeconds.divide(HOUR_IN_SECONDS, MathContext.DECIMAL32);
        return timeElapsedInHours;
    }

    public static void printResultString(Map<Integer, Floor> floors) {
        logger.info("Output:");
        floors.entrySet()
              .forEach(f -> logger.info(f.getValue().toString().replace(", ", "")));
    }

}
