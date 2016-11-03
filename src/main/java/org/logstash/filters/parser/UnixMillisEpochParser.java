package org.logstash.filters.parser;

import org.joda.time.Instant;

public class UnixMillisEpochParser implements TimestampParser {

  @Override
  public Instant parse(String value) {
    return new Instant(Long.parseLong(value));
  }

  @Override
  public Instant parseWithTimeZone(String value, String timezone) {
    return parse(value);
  }
}
