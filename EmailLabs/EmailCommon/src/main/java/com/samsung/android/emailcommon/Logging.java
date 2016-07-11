/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsung.android.emailcommon;

public class Logging {
    public static final String LOG_TAG = "Email";

    public final static String PERFORMANCE_TAG = "EmailPerformance";
    public static long startTime = 0, prevTime = 0;
    public static long startDownTime = 0, prevDownTime = 0;
    public static long startOpenTime = 0, prevOpenTime = 0;
}
