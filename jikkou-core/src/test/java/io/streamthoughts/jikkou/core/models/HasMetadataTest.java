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
package io.streamthoughts.jikkou.core.models;

import io.streamthoughts.jikkou.core.annotation.ApiVersion;
import io.streamthoughts.jikkou.core.annotation.Kind;
import io.streamthoughts.jikkou.core.annotation.Transient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HasMetadataTest {

    @Test
    void shouldGetApiVersion() {
        String apiVersion = Resource.getApiVersion(TransientTestResource.class);
        Assertions.assertEquals("version", apiVersion);
    }

    @Test
    void shouldGetKind() {
        String apiVersion = Resource.getKind(TransientTestResource.class);
        Assertions.assertEquals("kind", apiVersion);
    }

    @Test
    void shouldGetTrueForTransientResource() {
        boolean isTransient = Resource.isTransient(TransientTestResource.class);
        Assertions.assertTrue(isTransient);
    }

    @Test
    void shouldGetFalseForNonTransientResource() {
        boolean isTransient = Resource.isTransient(NonTransientTestResource.class);
        Assertions.assertFalse(isTransient);
    }

    private static class TestResource implements HasMetadata {

        @Override
        public ObjectMeta getMetadata() {
            return null;
        }

        @Override
        public HasMetadata withMetadata(ObjectMeta objectMeta) {
            return null;
        }

        @Override
        public String getApiVersion() {
            return null;
        }

        @Override
        public String getKind() {
            return null;
        }
    }
    @ApiVersion("version")
    @Kind("kind")
    static class NonTransientTestResource extends TestResource {}

    @ApiVersion("version")
    @Kind("kind")
    @Transient
    static class TransientTestResource extends TestResource {}
}