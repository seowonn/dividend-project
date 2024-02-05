package com.example.dividend.service;

import com.example.dividend.entity.CompanyEntity;
import com.example.dividend.entity.DividendEntity;
import com.example.dividend.exception.DividendException;
import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.dividend.model.type.ErrorCode.COMPANY_NAME_NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = "finance")
    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity =
                companyRepository.findByName(companyName)
                        .orElseThrow(
                                () -> new DividendException(COMPANY_NAME_NOT_FOUND)
                        );

        // 조회된 회사 ID로 배당금 정보를 조회
        List<DividendEntity> dividendEntities =
                dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(
                new Company(companyEntity.getTicker(), companyEntity.getName()),
                dividends);
    }
}
