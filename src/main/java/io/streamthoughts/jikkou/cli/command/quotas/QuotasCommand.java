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
package io.streamthoughts.jikkou.cli.command.quotas;

import io.streamthoughts.jikkou.api.change.ChangeResult;
import io.streamthoughts.jikkou.api.change.QuotaChange;
import io.streamthoughts.jikkou.api.change.QuotaChangeOptions;
import io.streamthoughts.jikkou.cli.command.WithSpecificationCommand;
import io.streamthoughts.jikkou.cli.command.quotas.subcommands.Alter;
import io.streamthoughts.jikkou.cli.command.quotas.subcommands.Apply;
import io.streamthoughts.jikkou.cli.command.quotas.subcommands.Create;
import io.streamthoughts.jikkou.cli.command.quotas.subcommands.Delete;
import io.streamthoughts.jikkou.cli.command.quotas.subcommands.Describe;
import io.streamthoughts.jikkou.api.config.JikkouConfig;
import io.streamthoughts.jikkou.api.config.JikkouParams;
import io.streamthoughts.jikkou.api.manager.KafkaQuotaManager;
import io.streamthoughts.jikkou.api.manager.KafkaResourceManager;
import io.streamthoughts.jikkou.api.manager.KafkaResourceUpdateContext;
import io.streamthoughts.jikkou.api.model.V1SpecObject;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

import java.util.Collection;
import java.util.List;

@CommandLine.Command(name = "quotas",
        headerHeading = "Usage:%n%n",
        descriptionHeading = "%nDescription:%n%n",
        parameterListHeading = "%nParameters:%n%n",
        optionListHeading = "%nOptions:%n%n",
        commandListHeading = "%nCommands:%n%n",
        synopsisHeading = "%n",
        header = "Apply the quotas changes described by your specs-file against the Kafka cluster you are currently pointing at.",
        description = "This command can be used to create, alter, delete or describe quotas on a remote Kafka cluster",
        subcommands = {
                Alter.class,
                Apply.class,
                Create.class,
                Delete.class,
                Describe.class,
                CommandLine.HelpCommand.class
        },
        mixinStandardHelpOptions = true)
public class QuotasCommand {

    public static abstract class Base extends WithSpecificationCommand<QuotaChange> {

        public abstract KafkaResourceManager.UpdateMode getUpdateMode();

        public abstract QuotaChangeOptions getChangeOptions();

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<ChangeResult<QuotaChange>> execute(final @NotNull List<V1SpecObject> objects) {
            final KafkaQuotaManager manager = JikkouParams.KAFKA_QUOTAS_MANAGER.get(JikkouConfig.get());

            return manager.update(
                    getUpdateMode(),
                    objects,
                    KafkaResourceUpdateContext.with(
                            QuotasCommand.Base.this::isResourceCandidate,
                            getChangeOptions(),
                            isDryRun()
                    )
            );
        }
    }
}