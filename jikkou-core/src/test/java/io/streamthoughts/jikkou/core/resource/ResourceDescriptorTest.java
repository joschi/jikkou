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
package io.streamthoughts.jikkou.core.resource;

import io.streamthoughts.jikkou.core.annotation.ApiVersion;
import io.streamthoughts.jikkou.core.annotation.Description;
import io.streamthoughts.jikkou.core.annotation.Kind;
import io.streamthoughts.jikkou.core.annotation.Names;
import io.streamthoughts.jikkou.core.models.HasMetadata;
import io.streamthoughts.jikkou.core.models.ResourceListObject;
import io.streamthoughts.jikkou.core.models.ResourceType;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceDescriptorTest {

    public static final String TEST_KIND = "Test";

    @Test
    void shouldReturnLowercaseKindWhenNoSingularName() {
        ResourceDescriptor descriptor = new ResourceDescriptor(
                ResourceType.of(TestResource.class),
                "",
                TestResource.class
        );
        Assertions.assertEquals(TEST_KIND.toLowerCase(Locale.ROOT), descriptor.singularName());
    }

    @Test
    void shouldReturnFalseWhenVerifyResourceListObjectForResource() {
        ResourceDescriptor descriptor = new ResourceDescriptor(
                ResourceType.of(TestResource.class),
                "",
                TestResource.class
        );
        Assertions.assertFalse(descriptor.isResourceListObject());
    }

    @Test
    void shouldReturnTrueWhenVerifyResourceListObjectForResourceList() {
        ResourceDescriptor descriptor = new ResourceDescriptor(
                ResourceType.of(TestListResource.class),
                "",
                TestListResource.class
        );
        Assertions.assertTrue(descriptor.isResourceListObject());
    }

    @ApiVersion("test.jikkou.io/v1beta2")
    @Kind("TestList")
    static abstract class TestListResource implements ResourceListObject<HasMetadata> {
    }

    @ApiVersion("test.jikkou.io/v1beta2")
    @Kind("Test")
    @Description("Test description")
    @Names(
            plural = "tests",
            singular = "test",
            shortNames = {"t", "ts"}
    )
    static abstract class TestResource implements HasMetadata {}
}