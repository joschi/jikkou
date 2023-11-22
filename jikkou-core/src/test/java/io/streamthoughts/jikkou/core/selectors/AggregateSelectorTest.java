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
package io.streamthoughts.jikkou.core.selectors;

import io.streamthoughts.jikkou.core.models.HasMetadata;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AggregateSelectorTest {

    @Test
    void shouldReturnAllSelectorExpression() {
        AggregateSelector selector = new AggregateSelector(List.of(
                getSelectorForExpressionString("expr1"),
                getSelectorForExpressionString("expr2")
        ));
        Assertions.assertEquals(List.of("expr1", "expr2"), selector.getSelectorExpressions());
    }

    @NotNull
    private static Selector getSelectorForExpressionString(String expr1) {
        return new Selector() {
            @Override
            public boolean apply(@NotNull HasMetadata resource) {
                return false;
            }

            @Override
            public List<String> getSelectorExpressions() {
                return List.of(expr1);
            }
        };
    }
}