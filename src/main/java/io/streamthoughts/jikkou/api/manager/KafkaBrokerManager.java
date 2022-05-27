/*
 * Copyright 2022 StreamThoughts.
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
package io.streamthoughts.jikkou.api.manager;

import io.streamthoughts.jikkou.api.change.ChangeComputer;
import io.streamthoughts.jikkou.api.change.ChangeResult;
import io.streamthoughts.jikkou.api.model.V1SpecObject;
import io.streamthoughts.jikkou.api.change.Change;
import io.streamthoughts.jikkou.api.model.V1BrokerObject;

import java.util.Collection;
import java.util.List;

/**
 * Base interface for managing Kafka Topics.
 */
public interface KafkaBrokerManager extends
        KafkaResourceManager<V1BrokerObject, Change<?>, ChangeComputer.Options, BrokerDescribeOptions> {

    /**
     * {@inheritDoc}
     */
    @Override
    default Collection<ChangeResult<Change<?>>> update(UpdateMode mode,
                                                       List<V1SpecObject> objects,
                                                       KafkaResourceUpdateContext<ChangeComputer.Options> context) {
        throw new UnsupportedOperationException();
    }

}