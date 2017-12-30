package dbappdemo;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class time {
    public static Timestamp UTCTS(OffsetDateTime x) {
        OffsetDateTime z = x.withOffsetSameInstant(ZoneOffset.UTC);
        return Timestamp.valueOf(LocalDateTime.ofInstant(z.toInstant(), ZoneOffset.UTC));
    }
    public static ZoneOffset tzLOC() {
        return OffsetDateTime.now().getOffset();
    }
    public static OffsetDateTime toODT(Timestamp x) {
        Instant y = x.toInstant();
        OffsetDateTime z0 = OffsetDateTime.of(x.toLocalDateTime(), ZoneOffset.UTC);
        OffsetDateTime z = z0.withOffsetSameInstant(tzLOC());
        return z;
    }
    private static boolean isLeapYear(int year) {
        Calendar x = Calendar.getInstance();
        x.set(Calendar.YEAR, year);
        return x.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }
    public static int lengthOfMonth(OffsetDateTime x) {
        return x.getMonth().length(isLeapYear(x.getYear()));
    }
    //need sunday = 0; otherwise sunday will be 7 and it'll break everything, here's the fix
    public static int WKDAY(OffsetDateTime x) { return x.getDayOfWeek().getValue() % 7; }
}
