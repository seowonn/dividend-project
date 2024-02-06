package com.example.dividend.service;

import com.example.dividend.aop.Scrapper;
import com.example.dividend.entity.CompanyEntity;
import com.example.dividend.entity.DividendEntity;
import com.example.dividend.exception.impl.AlreadyExistTickerException;
import com.example.dividend.exception.impl.NoCompanyException;
import com.example.dividend.exception.impl.NoTickerException;
import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie<String, String> trie;
    private final Scrapper yahooFinanceScrapper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists){
            throw new AlreadyExistTickerException();
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
            throw new NoTickerException();
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
                scrapedResult.getDividendEntities().stream()
                .map(e -> DividendEntity.builder()
                        .companyId(companyEntity.getId())
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build()
                ).toList();

        dividendRepository.saveAll(dividendEntities);

        return company;
    }

    public List<String> getCompanyNamesByKeyword(String keyword){
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities =
                companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);

        return companyEntities.stream()
                .map(CompanyEntity::getName)
                .collect(Collectors.toList());
    }

    public void addAutocompleteKeyword(String keyword){
        trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword){
        return (List<String>) new ArrayList<>(trie.prefixMap(keyword).keySet());
    }

    public void deleteAutocompleteKeyword(String keyword){
        trie.remove(keyword);
    }

    public String deleteCompany(String ticker) {
        CompanyEntity companyEntity = this.companyRepository.findByTicker(ticker)
                .orElseThrow(NoCompanyException::new);

        this.dividendRepository.deleteAllByCompanyId(companyEntity.getId());
        this.companyRepository.delete(companyEntity);

        // 자동 완성 기능을 위해 사용한 trie에 저장된 이름도 같이 지워줘야 한다.
        this.deleteAutocompleteKeyword(companyEntity.getName());
        return companyEntity.getName();
    }
}
