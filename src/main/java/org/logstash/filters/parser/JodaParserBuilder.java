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
import org.logstash.filters.DateFilter;

import java.util.*;

public class JodaParserBuilder {
  private DateTimeZone timezone;
  private Locale locale;
  private Map<String, List<String>> fieldPatterns = new TreeMap<>();

  public JodaParserBuilder() {
  }

  public void addFormat(String fieldref, String format) {
    List<String> parsers = getPatternsList(fieldref);

    switch (format) {
      case "ISO8601":
        addFormat(fieldref, "yyyy-MM-dd HH:mm:ss.SSSZ");
        addFormat(fieldref, "yyyy-MM-dd HH:mm:ss.SSS");
        addFormat(fieldref, "yyyy-MM-dd HH:mm:ss,SSSZ");
        addFormat(fieldref, "yyyy-MM-dd HH:mm:ss,SSS");
        break;
      default:
        parsers.add(format);
    }
  }

  private List<String> getPatternsList(String fieldref) {
    if (fieldPatterns.containsKey(fieldref)) {
      return fieldPatterns.get(fieldref);
    }
    return fieldPatterns.put(fieldref, new LinkedList<>());
  }

  private DateTimeFormatter buildDateParser(List<String> patterns) {
    DateTimeFormatterBuilder fb = new DateTimeFormatterBuilder();

    patterns.stream()
            .map(DateTimeFormat::forPattern)
            .forEach(fb::append);

    /* zone and locale can be null as permitted by joda-time. */
    return fb.toFormatter().withLocale(locale).withZone(timezone);
  }

  public JodaParser build() {
    Map<String, DateTimeFormatter> fieldParsers = new TreeMap<>();

    fieldPatterns.entrySet().stream()
            .forEach(entry -> fieldParsers.put(entry.getKey(), buildDateParser(entry.getValue())));

    return new JodaParser(fieldParsers);
  }
}
