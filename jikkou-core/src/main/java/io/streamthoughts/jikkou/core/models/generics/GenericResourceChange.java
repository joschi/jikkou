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
package io.streamthoughts.jikkou.core.models.generics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.streamthoughts.jikkou.core.annotation.ApiVersion;
import io.streamthoughts.jikkou.core.annotation.Description;
import io.streamthoughts.jikkou.core.annotation.Kind;
import io.streamthoughts.jikkou.core.annotation.Reflectable;
import io.streamthoughts.jikkou.core.models.HasMetadata;
import io.streamthoughts.jikkou.core.models.HasMetadataChange;
import io.streamthoughts.jikkou.core.models.ObjectMeta;
import io.streamthoughts.jikkou.core.models.Resource;
import jakarta.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Description("")
@JsonPropertyOrder({
        "apiVersion",
        "kind",
        "metadata",
        "change"
})
@ApiVersion("core.jikkou.io/v1beta2")
@Kind("GenericResourceChange")
@JsonDeserialize
@Reflectable
public final class GenericResourceChange implements HasMetadataChange<GenericChange> {

    private final String kind;
    private final String apiVersion;
    private final ObjectMeta metadata;
    private final GenericChange change;


    @ConstructorProperties({
            "apiVersion",
            "kind",
            "metadata",
            "change"
    })
    public GenericResourceChange(@NotNull String kind,
                                 @NotNull String apiVersion,
                                 @NotNull ObjectMeta metadata,
                                 @NotNull GenericChange change) {
        this.kind = kind;
        this.apiVersion = apiVersion;
        this.metadata = metadata;
        this.change = change;
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("kind")
    @Override
    public String getKind() {
        return Optional.ofNullable(kind).orElse(Resource.getKind(this.getClass()));
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("apiVersion")
    @Override
    public String getApiVersion() {
        return Optional.ofNullable(apiVersion).orElse(Resource.getApiVersion(this.getClass()));
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("metadata")
    @Override
    public ObjectMeta getMetadata() {
        return Optional.ofNullable(metadata).orElse(new ObjectMeta());
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public HasMetadata withMetadata(ObjectMeta objectMeta) {
        return new GenericResourceChange(apiVersion, kind, objectMeta, change);
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("change")
    @Override
    public GenericChange getChange() {
        return change;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public String toString() {
        return "GenericResourceChange[" +
                "kind=" + kind +
                ", apiVersion=" + apiVersion +
                ", metadata=" + metadata +
                ", change=" + change +
                ']';
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GenericResourceChange) obj;
        return Objects.equals(this.kind, that.kind) &&
                Objects.equals(this.apiVersion, that.apiVersion) &&
                Objects.equals(this.metadata, that.metadata) &&
                Objects.equals(this.change, that.change);
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public int hashCode() {
        return Objects.hash(kind, apiVersion, metadata, change);
    }
}