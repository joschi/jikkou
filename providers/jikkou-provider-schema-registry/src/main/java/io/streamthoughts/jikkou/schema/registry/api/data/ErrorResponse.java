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
package io.streamthoughts.jikkou.schema.registry.api.data;

import io.streamthoughts.jikkou.core.annotation.Reflectable;
import java.beans.ConstructorProperties;

/**
 * Schema Registry - Error Response
 *
 * @param errorCode the error name.
 * @param message   the error message.
 */
@Reflectable
public record ErrorResponse(int errorCode, String message) {

    /**
     * Creates a new {@link ErrorResponse} instance.
     */
    @ConstructorProperties({
            "error_code",
            "message"
    })
    public ErrorResponse { }

}