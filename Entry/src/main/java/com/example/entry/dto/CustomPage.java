package com.example.entry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Custom Pagination Data")
public class CustomPage {

    @NotNull
    @Schema(description = "Pagination Records", example = "[{},{},{}]")
    private Object records;

    @NotNull
    @Schema(description = "Number of Records", example = "20")
    private long total;

    @NotNull
    @Schema(description = "Page Size", example = "10")
    private long size;

    @NotNull
    @Schema(description = "Current Page Number", example = "1")
    private long current;

    @NotNull
    @Schema(description = "Total Page Number", example = "5")
    private long pages;

}
