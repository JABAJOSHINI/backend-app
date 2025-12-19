package com.digital.digitalcommunicationservice.utils;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Log4j2
public class Utils {
    public static LocalDateTime getCurrentISTTime() {
        return new Date().toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
    }
}
