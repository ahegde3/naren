/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.secfunc;

import com.samsung.android.emailcommon.utility.Utility;

import org.apache.james.mime4j.Log;
import org.apache.james.mime4j.LogFactory;
import org.apache.james.mime4j.secfunc.datetime.DateTime;
import org.apache.james.mime4j.secfunc.datetime.parser.ParseException;

import java.util.Date;

public class DateTimeField extends Field {
    private Date date;
    private ParseException parseException;

    protected DateTimeField(String name, String body, String raw, Date date,
            ParseException parseException) {
        super(name, body, raw);
        this.date = date;
        this.parseException = parseException;
    }

    public Date getDate() {
        return date;
    }

    public ParseException getParseException() {
        return parseException;
    }

    public static class Parser implements FieldParser {
        private static Log log = LogFactory.getLog(/*Parser.class*/);

        public Field parse(final String name, String body, final String raw) {
            Date date1 = null;
            ParseException parseException1 = null;
            // BEGIN android-changed
            body = Utility.cleanUpMimeDate(body);
            // END android-changed
            try {
                date1 = DateTime.parse(body).getDate();
            } catch (ParseException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Parsing value '" + body + "': " + e.getMessage());
                }
                parseException1 = e;
            }
            return new DateTimeField(name, body, raw, date1, parseException1);
        }
    }
}
