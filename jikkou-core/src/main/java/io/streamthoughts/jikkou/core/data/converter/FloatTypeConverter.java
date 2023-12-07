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
package io.streamthoughts.jikkou.core.data.converter;

import io.streamthoughts.jikkou.core.data.TypeConversionException;
import io.streamthoughts.jikkou.core.data.TypeConverter;
import java.math.BigDecimal;

public final class FloatTypeConverter implements TypeConverter<Float> {

    /**
     * {@inheritDoc}
     **/
    @Override
    public Float convertValue(Object value) {
        if (value == null) return null;

        if (value instanceof String string) {
            return new BigDecimal(string).floatValue();
        }
        if (value instanceof Number number) {
            return number.floatValue();
        }

        throw new TypeConversionException(String.format("Cannot parse 32-bits float content from \"%s\"", value));
    }
}
