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
package io.streamthoughts.jikkou.api.validations;

import io.streamthoughts.jikkou.api.model.V1AccessRoleObject;
import io.streamthoughts.jikkou.api.model.V1SecurityObject;
import io.streamthoughts.jikkou.api.model.V1SpecObject;
import io.streamthoughts.jikkou.api.resources.Named;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class NoDuplicateRolesAllowedValidation implements Validation {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final @NotNull V1SpecObject specsObject) throws ValidationException {
        final Optional<V1SecurityObject> security = specsObject.security();
        if (security.isEmpty()) return;

        final Collection<V1AccessRoleObject> roles = security.get().roles();

        if (roles.isEmpty()) return;

        final Map<String, List<V1AccessRoleObject>> groupedByName = Named.groupByName(roles);

        final Set<String> duplicates = groupedByName.entrySet()
                .stream()
                .filter(it -> it.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!duplicates.isEmpty()) {
            throw new ValidationException("Duplicates 'name' in specs.security.roles: " + duplicates, this);
        }
    }
}