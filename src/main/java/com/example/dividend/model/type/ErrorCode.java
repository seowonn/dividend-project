package com.example.dividend.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_MONTH_VALUE("Unexpected Month enum value"),
    TICKER_NOT_FOUND("Failed to scrap ticker"),
    TICKER_ALREADY_EXISTS("Already exists ticker");

    private final String description;
}
