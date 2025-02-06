package com.fortune_api.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Log {
    private static Log instance;
    File logFile = null;

    private Log() {
        this.logFile = new File("./src/main/java/resources/log", "log.txt");
        try {this.logFile.createNewFile();} catch (IOException e) {}
    }

    public static Log getInstance() {
        return instance == null ? (instance = new Log()) : instance;
    }

    public void writeLog(String message) {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFile, true));
            int var10001 = localDate.getDayOfMonth();
            writer.write("[" + localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear() + " | " + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + "] -- " + message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
        }

    }
}
