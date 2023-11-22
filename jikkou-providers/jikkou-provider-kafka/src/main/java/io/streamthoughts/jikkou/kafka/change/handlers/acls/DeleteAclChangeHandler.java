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
package io.streamthoughts.jikkou.kafka.change.handlers.acls;

import io.streamthoughts.jikkou.common.utils.Pair;
import io.streamthoughts.jikkou.core.models.HasMetadataChange;
import io.streamthoughts.jikkou.core.reconcilier.ChangeHandler;
import io.streamthoughts.jikkou.core.reconcilier.ChangeMetadata;
import io.streamthoughts.jikkou.core.reconcilier.ChangeResponse;
import io.streamthoughts.jikkou.core.reconcilier.ChangeType;
import io.streamthoughts.jikkou.kafka.adapters.KafkaAclBindingAdapter;
import io.streamthoughts.jikkou.kafka.change.AclChange;
import io.streamthoughts.jikkou.kafka.model.KafkaAclBinding;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteAclsResult;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.jetbrains.annotations.NotNull;

public class DeleteAclChangeHandler implements KafkaAclChangeHandler {

    private final AdminClient client;

    /**
     * Creates a new {@link DeleteAclChangeHandler} instance.
     *
     * @param client the {@link AdminClient}.
     */
    public DeleteAclChangeHandler(@NotNull final AdminClient client) {
        this.client = Objects.requireNonNull(client, "client cannot not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ChangeType> supportedChangeTypes() {
        return Set.of(ChangeType.DELETE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChangeResponse<AclChange>> apply(@NotNull final List<HasMetadataChange<AclChange>> items) {
        Map<KafkaAclBinding, HasMetadataChange<AclChange>> data = items
                .stream()
                .peek(it -> ChangeHandler.verify(this, it))
                .map(it -> Pair.of(it.getChange().acl(), it))
                .collect(Collectors.toMap(Pair::_1, Pair::_2));

        List<AclBindingFilter> bindings = data.keySet().stream()
                .map(KafkaAclBindingAdapter::toAclBindingFilter)
                .toList();

        DeleteAclsResult result = client.deleteAcls(bindings);

        Map<AclBindingFilter, KafkaFuture<DeleteAclsResult.FilterResults>> values = result.values();
        return values.entrySet()
                .stream()
                .map(entry -> Pair.of(KafkaAclBindingAdapter.fromAclBindingFilter(entry.getKey()), entry.getValue()))
                .map(pair -> {
                    HasMetadataChange<AclChange> change = data.get(pair._1());
                    CompletableFuture<DeleteAclsResult.FilterResults> future = pair._2()
                            .toCompletionStage()
                            .toCompletableFuture();
                    return new ChangeResponse<>(change, future.thenApply(ignore -> ChangeMetadata.empty()));
                })
                .toList();
    }
}