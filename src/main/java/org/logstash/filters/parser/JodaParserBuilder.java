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

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class JodaParserBuilder {
  private DateTimeZone timezone;
  private Locale locale;
  private List<String> patterns = new LinkedList<>();

  public JodaParserBuilder() {
  }

  public void addPattern(String pattern) {
    switch (pattern) {
      case "ISO8601":
        addPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
        addPattern("yyyy-MM-dd HH:mm:ss.SSS");
        addPattern("yyyy-MM-dd HH:mm:ss,SSSZ");
        addPattern("yyyy-MM-dd HH:mm:ss,SSS");
        break;
      default:
        patterns.add(pattern);
    }
  }

  private DateTimeFormatter buildDateParser() {
    // XXX: What about patterns with no year?
    DateTimeFormatterBuilder fb = new DateTimeFormatterBuilder();
    patterns.stream()
            .map(DateTimeFormat::forPattern)
            .forEach(fb::append);

    /* zone and locale can be null as permitted by joda-time. */
    return fb.toFormatter().withLocale(locale).withZone(timezone).withOffsetParsed();
  }

  public JodaParser build() {
    if (patterns.isEmpty()) {
      throw new UnsupportedOperationException("Cannot build JodaParser because no patterns were given");
    }
    return new JodaParser(buildDateParser());
  }
}
