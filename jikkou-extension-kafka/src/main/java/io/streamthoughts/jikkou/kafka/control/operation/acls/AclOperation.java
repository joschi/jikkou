/*
 * Copyright 2020 StreamThoughts.
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
package io.streamthoughts.jikkou.kafka.control.operation.acls;

import io.streamthoughts.jikkou.api.control.Description;
import io.streamthoughts.jikkou.api.control.ExecutableOperation;
import io.streamthoughts.jikkou.kafka.control.change.AclChange;
import io.streamthoughts.jikkou.kafka.model.AccessControlPolicy;
import io.vavr.concurrent.Future;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface AclOperation extends ExecutableOperation<AclChange, AccessControlPolicy, Void> {

    /**
     * {@inheritDoc}
     */
    @Override
    Description getDescriptionFor(@NotNull final AclChange change);

    /**
     * {@inheritDoc}
     */
    @Override
    boolean test(final AclChange change);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull Map<AccessControlPolicy, List<Future<Void>>> doApply(@NotNull final Collection<AclChange> changes);

}