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
package io.streamthoughts.kafka.specs.operation;

import io.streamthoughts.kafka.specs.Description;
import io.streamthoughts.kafka.specs.resources.acl.AccessControlPolicy;
import io.streamthoughts.kafka.specs.change.AclChange;
import io.streamthoughts.kafka.specs.change.AclChanges;
import io.streamthoughts.kafka.specs.change.Change;
import io.streamthoughts.kafka.specs.internal.DescriptionProvider;
import io.streamthoughts.kafka.specs.internal.FutureUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteAclsResult;
import org.apache.kafka.common.acl.AccessControlEntryFilter;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DeleteAclsOperation implements AclOperation {

    public static final DescriptionProvider<AccessControlPolicy> DESCRIPTION = (r) -> (Description.Create) () -> {
        return String.format("Delete ACL (%s %s to %s %s:%s:%s)",
                r.permission(),
                r.principal(),
                r.operation(),
                r.resourceType(),
                r.patternType(),
                r.resourcePattern());
    };

    private final AclBindingConverter converter = new AclBindingConverter();

    private final AdminClient adminClient;

    /**
     * Creates a new {@link DeleteAclsOperation} instance.
     *
     * @param adminClient   the {@link AdminClient}.
     */
    public DeleteAclsOperation(@NotNull final AdminClient adminClient) {
        this.adminClient = Objects.requireNonNull(adminClient, "'adminClient should not be null'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Description getDescriptionFor(@NotNull final AclChange change) {
        return DESCRIPTION.getForResource(change.getAccessControlPolicy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean test(final AclChange change) {
        return change.getOperation() == Change.OperationType.DELETE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<AccessControlPolicy, CompletableFuture<Void>> apply(final @NotNull AclChanges changes) {
        List<AclBindingFilter> bindings = changes
                .all()
                .stream()
                .map(AclChange::getAccessControlPolicy)
                .map(converter::toAclBindingFilter)
                .collect(Collectors.toList());

        DeleteAclsResult result = adminClient.deleteAcls(bindings);
         return result.values().entrySet()
              .stream()
              .collect(Collectors.toMap(
                      e -> converter.fromAclBindingFilter(e.getKey()),
                      e -> FutureUtils.toVoidCompletableFuture(e.getValue()))
              );
    }

    private static class AclBindingConverter {

        AclBindingFilter toAclBindingFilter(final AccessControlPolicy rule) {
            return new AclBindingFilter(
                    new ResourcePatternFilter(rule.resourceType(), rule.resourcePattern(), rule.patternType()),
                    new AccessControlEntryFilter(rule.principal(), rule.host(), rule.operation(), rule.permission())
            );
        }

        AccessControlPolicy fromAclBindingFilter(final AclBindingFilter binding) {
            final AccessControlEntryFilter entryFilter = binding.entryFilter();
            final ResourcePatternFilter pattern = binding.patternFilter();

            String principal = entryFilter.principal();
            String[] principalTypeAndName = principal.split(":");
            return AccessControlPolicy.newBuilder()
                    .withResourcePattern(pattern.name())
                    .withPatternType(pattern.patternType())
                    .withResourceType(pattern.resourceType())
                    .withOperation(entryFilter.operation())
                    .withPermission(entryFilter.permissionType())
                    .withHost(entryFilter.host())
                    .withPrincipalName(principalTypeAndName[1])
                    .withPrincipalType(principalTypeAndName[0])
                    .build();
        }
    }
}