package com.example.dividend.aop;

import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;

public interface Scrapper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
