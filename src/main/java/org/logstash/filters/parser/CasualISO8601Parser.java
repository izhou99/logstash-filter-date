package org.logstash.filters.parser;

import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Created by jls on 11/2/16.
 */
public class CasualISO8601Parser implements TimestampParser {
  private static final DateTimeFormatter parser;

  static {
    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
    builder.append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSZ"));
    builder.append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    builder.append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSSZ"));
    builder.append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    parser = builder.toFormatter();
  }

  public CasualISO8601Parser() {
    // nothing
  }

  @Override
  public Instant parse(String value) {
    return new Instant(parser.parseMillis(value));
  }
}
