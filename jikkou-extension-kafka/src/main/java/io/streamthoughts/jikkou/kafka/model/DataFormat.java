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
package io.streamthoughts.jikkou.kafka.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.streamthoughts.jikkou.kafka.internals.DataSerde;
import io.streamthoughts.jikkou.kafka.internals.serdes.KafkaJsonSerdes;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * The formats supported by key/value Kafka record.
 */
public enum DataFormat {

    BINARY {
        /** {@inheritDoc} **/
        @Override
        public DataSerde getDataSerde() {
            return new BinarySerde();
        }
    },
    STRING {
        /** {@inheritDoc} **/
        @Override
        public DataSerde getDataSerde() {
            return new StringSerde();
        }
    },
    JSON {
        /** {@inheritDoc} **/
        @Override
        public DataSerde getDataSerde() {
            return new JsonSerde();
        }
    },
    LONG {
        /** {@inheritDoc} **/
        @Override
        public DataSerde getDataSerde() {
            return new LongSerde();
        }
    };

    /**
     * Gets the {@link DataSerde} for this format.
     *
     * @return  a new {@link DataSerde}.
     */
    public abstract DataSerde getDataSerde();

    /**
     * Data Serde for Json.
     */
    static class JsonSerde implements DataSerde {
        @Override
        public Optional<ByteBuffer> serialize(String topicName,
                                              DataHandle data,
                                              Map<String, Object> properties,
                                              boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            try (KafkaJsonSerdes serdes = new KafkaJsonSerdes(properties, isForRecordKey)) {
                byte[] bytes = serdes.serialize(topicName, data.value());
                return Optional.of(ByteBuffer.wrap(bytes));
            }
        }

        @Override
        public Optional<DataHandle> deserialize(String topicName,
                                                ByteBuffer data,
                                                Map<String, Object> properties,
                                                boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            try (KafkaJsonSerdes serdes = new KafkaJsonSerdes(properties, isForRecordKey)) {
                JsonNode output = serdes.deserialize(topicName, data.array());
                return Optional.ofNullable(output).map(DataHandle::new);
            }
        }
    }

    /**
     * Data Serde for Long.
     */
    static class LongSerde implements DataSerde {
        @Override
        public Optional<ByteBuffer> serialize(String topicName,
                                              DataHandle data,
                                              Map<String, Object> properties,
                                              boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            try (var serializer = new LongSerializer()) {
                byte[] bytes = serializer.serialize(topicName, data.value().asLong());
                return Optional.of(ByteBuffer.wrap(bytes));
            }
        }

        @Override
        public Optional<DataHandle> deserialize(String topicName,
                                                ByteBuffer data,
                                                Map<String, Object> properties,
                                                boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            try (var deserializer = new LongDeserializer()) {
                Long deserialize = deserializer.deserialize(topicName, data.array());
                return Optional.of(new LongNode(deserialize)).map(DataHandle::new);
            }
        }
    }

    /**
     * Data Serde for binary content encoded in base64.
     */
    static class BinarySerde implements DataSerde {
        @Override
        public Optional<ByteBuffer> serialize(String topicName,
                                              DataHandle data,
                                              Map<String, Object> properties,
                                              boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            String encoded = data.value().asText();
            return Optional.of(ByteBuffer.wrap(Base64.getDecoder().decode(encoded)));
        }

        @Override
        public Optional<DataHandle> deserialize(String topicName,
                                                ByteBuffer data,
                                                Map<String, Object> properties,
                                                boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            byte[] array = data.array();
            return Optional
                    .of(Base64.getEncoder().encodeToString(array))
                    .map(TextNode::new)
                    .map(DataHandle::new);
        }
    }

    /**
     * Data Serde for Long.
     */
    static class StringSerde implements DataSerde {
        /** {@inheritDoc} **/
        @Override
        public Optional<ByteBuffer> serialize(String topicName,
                                              DataHandle data,
                                              Map<String, Object> properties,
                                              boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            try (StringSerializer serializer = new StringSerializer()) {
                serializer.configure(properties, isForRecordKey);
                byte[] bytes = serializer.serialize(topicName, data.value().asText());
                return Optional.of(ByteBuffer.wrap(bytes));
            }
        }
        /** {@inheritDoc} **/
        @Override
        public Optional<DataHandle> deserialize(String topicName,
                                                ByteBuffer data,
                                                Map<String, Object> properties,
                                                boolean isForRecordKey) {
            if (data == null) return Optional.empty();
            try (StringDeserializer deserializer = new StringDeserializer()) {
                deserializer.configure(properties, isForRecordKey);
                String output = deserializer.deserialize(topicName, data.array());
                return Optional
                        .ofNullable(output)
                        .map(TextNode::new)
                        .map(DataHandle::new);
            }
        }
    }
}