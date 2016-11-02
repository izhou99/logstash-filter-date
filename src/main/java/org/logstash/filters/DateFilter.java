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

package org.logstash.filters;

import org.joda.time.Instant;
import org.logstash.Event;
import org.logstash.Timestamp;
import org.logstash.filters.parser.TimestampParser;
import org.logstash.filters.parser.JodaParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DateFilter {
  private String sourceField;
  private TimestampParser[] parsers;
  private String targetField;
  private String[] tagOnFailure;

  public static void configure(Map<String, Object> settings) {
    // ???
  }

  public DateFilter(String sourceField, List<TimestampParser> parsers, String targetField, List<String> tagOnFailure) {
    this.sourceField = sourceField;
    this.parsers = parsers.toArray(new TimestampParser[0]);
    this.targetField = targetField;
    this.tagOnFailure = tagOnFailure.toArray(new String[0]);
  }

  public void register() {
    // Nothing to do.
  }



  public Event[] receive(List<org.logstash.ext.JrubyEventExtLibrary.RubyEvent> rubyEvents) {
    Event[] events = rubyEvents.stream().map(e -> e.getEvent()).toArray(size -> new Event[size]);
    return receive(events);
  }

  public Event[] receive(Event[] events) {
    System.out.println("OKKKK");
    for (Event event : events) {
      // XXX: Check for cast failures
      String input = (String) event.getField(sourceField);
      boolean success = false;
      for (TimestampParser parser : parsers) {
        try {
          Instant instant = parser.parse(input);
          event.setField(targetField, new Timestamp(instant.getMillis()));
          success = true;
        } catch (IllegalArgumentException e) {
          // Parsing failed due to invalid input.
          // Keep going
        }
      }

      if (!success) {
        for (String t : tagOnFailure) {
          event.tag(t);
        }
      }


      // no new events to add
    }
    return new Event[0];
  }
}
