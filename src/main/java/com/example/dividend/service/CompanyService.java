package com.example.dividend.service;

import com.example.dividend.aop.Scrapper;
import com.example.dividend.entity.CompanyEntity;
import com.example.dividend.entity.DividendEntity;
import com.example.dividend.exception.DividendException;
import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.example.dividend.model.type.ErrorCode.TICKER_ALREADY_EXISTS;
import static com.example.dividend.model.type.ErrorCode.TICKER_NOT_FOUND;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scrapper yahooFinanceScrapper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists){
            throw new DividendException(TICKER_ALREADY_EXISTS);
        }
        return storeCompanyAndDividend(ticker);
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable){
        return companyRepository.findAll(pageable);
    }

    private Company storeCompanyAndDividend(String ticker) {
        // ticker를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScrapper.scrapCompanyByTicker(ticker);

        if (ObjectUtils.isEmpty(company)) {
            throw new DividendException(TICKER_NOT_FOUND);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = yahooFinanceScrapper.scrap(company);

        // 스그래핑된 결과를 반환함.
        CompanyEntity companyEntity = companyRepository.save(
                CompanyEntity.builder()
                        .ticker(company.getTicker())
                        .name(company.getName())
                        .build()
        );

        List<DividendEntity> dividendEntities =
                scrapedResult.getDividends().stream()
                .map(e -> DividendEntity.builder()
                        .companyId(companyEntity.getId())
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build()
                ).toList();

        dividendRepository.saveAll(dividendEntities);

        return company;
    }
}
