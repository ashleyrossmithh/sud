package sud.core.text;


import java.text.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Стандартный MessageFormat не умеет работать с LocalDate и LocalTime.
 * <p>
 * Данный фрагмент кода заимствован.
 * <p>
 * Возможно, его следует переписать более оптимальным способом.
 * Но при переписывании учесть,
 * что вроде бы какой-то из DateFormat-ов (работающих со старым типом даты java.util.Date) потоконебезопасен
 */
public class Message {

    private Message() {
    }

    public static String format(String pattern, Object... arguments) {
        MessageFormat mf = new MessageFormat(pattern);

        int i = 0;
        for (Format format : mf.getFormats()) {
            if (format instanceof DateFormat) {
                mf.setFormat(i, new DateFormatWrapper(format));
            }
            i++;
        }

        return mf.format(arguments);
    }

    private static class DateFormatWrapper extends Format {

        private final Format inner;

        DateFormatWrapper(Format format) {
            this.inner = format;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (obj instanceof TemporalAccessor ta) {
                final Instant instant;
                if (obj instanceof LocalDate ld) {
                    instant = ld.atStartOfDay(ZoneId.systemDefault()).toInstant();
                } else if (obj instanceof LocalDateTime ldt) {
                    instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
                } else {
                    instant = Instant.from(ta);
                }
                obj = Date.from(instant);
            }
            return inner.format(obj, toAppendTo, pos);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return inner.parseObject(source, pos);
        }
    }
}
