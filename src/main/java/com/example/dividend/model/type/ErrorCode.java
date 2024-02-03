package com.example.dividend.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_MONTH_VALUE("Unexpected Month enum value"),
    TICKER_NOT_FOUND("Failed to scrap ticker"),
    TICKER_ALREADY_EXISTS("Already exists ticker"),
    COMPANY_NAME_NOT_FOUND("CompanyName doesn't exist");

    private final String description;
}
