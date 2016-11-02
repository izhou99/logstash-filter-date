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

import org.joda.time.Instant;

public class TAI64NParser implements TimestampParser {
  @Override
  public Instant parse(String value) {
    long secondsSinceEpoch = Long.parseLong(value.substring(0, 15), 16);
    int nanoseconds = Integer.parseInt(value.substring(16, 23), 16);
    // epochSeconds = ((date[0...16].hex) & ((1<<62) - 1))
    // nanoSeconds = date[16..24].hex
    return new Instant(secondsSinceEpoch * 1000 + (nanoseconds / 1_000_000));
  }
}
