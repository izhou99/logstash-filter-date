package org.logstash.filters.parser;

import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by jls on 11/2/16.
 */
public class CasualISO8601Parser implements TimestampParser {
  private static final DateTimeFormatter[] parsers;

  static {
    parsers = new DateTimeFormatter[] {
            ISODateTimeFormat.dateTimeParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSZ"),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSSZ"),
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS")
    };
  }

  public CasualISO8601Parser() {
    // nothing
  }

  @Override
  public Instant parse(String value) {
    RuntimeException lastException = null;
    for (DateTimeFormatter parser : parsers) {
      try {
        return new Instant(parser.parseMillis(value));
      } catch (IllegalArgumentException e) {
        lastException = e;
      }
    }

    throw lastException;
  }

  @Override
  public Instant parseWithTimeZone(String value, String timezone) {
    return parse(value);
  }
}
