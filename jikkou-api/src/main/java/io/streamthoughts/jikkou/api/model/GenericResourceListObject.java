/*
 * Copyright 2023 StreamThoughts.
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
package io.streamthoughts.jikkou.api.model;

import io.streamthoughts.jikkou.api.selector.ResourceSelector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable list of resource objects.
 */
public final class GenericResourceListObject implements HasItems  {

    public static GenericResourceListObject of(final HasItems items) {
        return new GenericResourceListObject(items.getItems());
    }

    public static GenericResourceListObject of(final HasMetadata... resources) {
        return new GenericResourceListObject(Arrays.asList(resources));
    }

    public static GenericResourceListObject of(final List<? extends HasMetadata> resources) {
        return new GenericResourceListObject(resources);
    }

    private final List<HasMetadata> items;

    /**
     * Creates a new {@link GenericResourceListObject} instance.
     *
     * @param resources the list of resources.
     */
    public GenericResourceListObject(@NotNull List<? extends HasMetadata> resources) {
        this.items = Collections.unmodifiableList(resources);
    }

    /** {@inheritDoc} */
    @Override
    public List<? extends HasMetadata> getItems() {
        return items;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public List<HasMetadata> getAllMatching(final @NotNull List<ResourceSelector> selectors) {
        return (List<HasMetadata>) HasItems.super.getAllMatching(selectors);
    }
    
}