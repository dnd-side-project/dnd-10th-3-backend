package dnd.donworry.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class TimeUtil {

	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static String toTimeStampString(LocalDateTime localDateTime) {
		return String.valueOf(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
	}
}
