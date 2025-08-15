package com.example.ie_um.resource.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ResourceListResDto(
        @JsonProperty("places")
        List<ResourceResDto> places,

        @JsonProperty("total_count")
        int totalCount,

        @JsonProperty("query_info")
        QueryInfoDto queryInfo
) {
}
