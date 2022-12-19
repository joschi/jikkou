/*
 * Copyright 2021 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.jikkou.api.extensions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ExtensionDescriptor<T extends Extension>(
        @JsonIgnore @NotNull Class<T> clazz,
        @JsonProperty("class") @NotNull String classType,
        @JsonProperty("aliases") @NotNull List<String> aliases,
        @JsonProperty("type") @Nullable String type,
        @JsonProperty("description") @Nullable String description
        ) implements Comparable<ExtensionDescriptor<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull ExtensionDescriptor<T> that) {
        return that.classType().compareTo(this.classType());
    }
}