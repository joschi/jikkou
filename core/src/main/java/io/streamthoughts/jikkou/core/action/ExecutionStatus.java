/*
 * Copyright 2023 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.jikkou.core.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.streamthoughts.jikkou.common.utils.Enums;
import org.jetbrains.annotations.Nullable;

/**
 * Status of the execution of an action.
 */
public enum ExecutionStatus {
    /**
     * Action was executed successfully.
     **/
    SUCCEEDED,
    /**
     * Action execution has timed out.
     */
    TIMED_OUT,
    /**
     * Action was executed with one or more errors.
     **/
    FAILED;

    @JsonCreator
    public static ExecutionStatus getForNameIgnoreCase(final @Nullable String str) {
        return Enums.getForNameIgnoreCase(str, ExecutionStatus.class);
    }
}