/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.logstash.filters.parser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.TimeZone;

public class TimestampParserFactory {
  private DateTimeZone timezone;
  private Locale locale;

  private static final String ISO8601 = "ISO8601";
  private static final String UNIX = "UNIX";
  private static final String UNIX_MS = "UNIX_MS";
  private static final String TAI64N = "TAI64N";

  public static TimestampParser makeParser(String pattern, Locale locale, DateTimeZone zone) {
    switch (pattern) {
      case ISO8601: // Short-hand for a few ISO8601-ish formats
        return new CasualISO8601Parser();
      case UNIX: // Unix epoch in seconds
        return new UnixEpochParser();
      case TAI64N: // TAI64N format
        return new TAI64NParser();
      case UNIX_MS: // Unix epoch in milliseconds
        return (value) -> new Instant(Long.parseLong(value));
      default:
        return new JodaParser(pattern, locale, zone);
    }
  }
}
