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
package io.streamthoughts.jikkou.kafka.change.topics;

import io.streamthoughts.jikkou.common.utils.CollectionUtils;
import io.streamthoughts.jikkou.core.models.change.ResourceChange;
import io.streamthoughts.jikkou.core.reconciler.ChangeMetadata;
import io.streamthoughts.jikkou.core.reconciler.ChangeResponse;
import io.streamthoughts.jikkou.core.reconciler.Operation;
import io.streamthoughts.jikkou.core.reconciler.TextDescription;
import io.streamthoughts.jikkou.core.reconciler.change.BaseChangeHandler;
import io.streamthoughts.jikkou.kafka.internals.Futures;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.kafka.clients.admin.AdminClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default command to delete multiple topics.
 */
public final class DeleteTopicChangeHandler extends BaseChangeHandler<ResourceChange> {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteTopicChangeHandler.class);

    private final AdminClient client;

    /**
     * Creates a new {@link DeleteTopicChangeHandler} instance.
     *
     * @param client the {@link AdminClient} to be used.
     */
    public DeleteTopicChangeHandler(final AdminClient client) {
        super(Operation.DELETE);
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextDescription describe(@NotNull final ResourceChange change) {
        return TopicChange.getDescription(change);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull List<ChangeResponse<ResourceChange>> handleChanges(
            final @NotNull List<ResourceChange> changes) {
        List<String> topics = changes
                .stream()
                .map(it -> it.getMetadata().getName())
                .collect(Collectors.toList());

        final Map<String, CompletableFuture<Void>> results = new HashMap<>(client.deleteTopics(topics).topicNameValues())
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Futures.toCompletableFuture(e.getValue())));

        Map<String, ResourceChange> changesByTopicName = CollectionUtils
                .keyBy(changes, it -> it.getMetadata().getName());

        return results.entrySet()
                .stream()
                .map(e -> {
                    CompletableFuture<ChangeMetadata> future = e.getValue().thenApply(unused -> {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("Completed deletion of Kafka topic {}", e.getKey());
                        }
                        return ChangeMetadata.empty();
                    });
                    ResourceChange item = changesByTopicName.get(e.getKey());
                    return new ChangeResponse<>(item, future);
                })
                .toList();
    }

}