package com.example.dividend.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapedResult {
    private Company company;
    private List<Dividend> dividends;
}
