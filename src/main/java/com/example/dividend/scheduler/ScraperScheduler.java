package com.example.dividend.scheduler;

import com.example.dividend.aop.Scrapper;
import com.example.dividend.entity.CompanyEntity;
import com.example.dividend.entity.DividendEntity;
import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.type.CacheKey;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {
    
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scrapper yahooFinanceScrapper;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for(CompanyEntity company : companies){
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = yahooFinanceScrapper.scrap(
                    new Company(company.getTicker(), company.getName())
            );

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividendEntities().stream()
                    // dividend 모델을 dividend entity로 매핑
                    .map(e -> DividendEntity.builder()
                            .companyId(company.getId())
                            .date(e.getDate())
                            .dividend(e.getDividend())
                            .build())
                    // element를 하나씩 dividend repository에 존재하지 않는 경우 삽입
                    .forEach(e -> {
                        boolean exists =
                                dividendRepository.existsByCompanyIdAndDate(
                                e.getCompanyId(), e.getDate()
                        );
                        if(!exists){
                            dividendRepository.save(e);
                        }
                    });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지 시킴
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
