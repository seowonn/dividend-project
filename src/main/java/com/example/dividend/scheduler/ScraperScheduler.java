package com.example.dividend.scheduler;

import com.example.dividend.aop.Scrapper;
import com.example.dividend.entity.CompanyEntity;
import com.example.dividend.entity.DividendEntity;
import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import javassist.Loader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {
    
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scrapper yahooFinanceScrapper;

    @Scheduled(fixedDelay = 1000)
    public void test1() throws InterruptedException {
        Thread.sleep(10000); // 10초간 일시정지
        System.out.println(Thread.currentThread().getName() + " -> 테스트 1 : " +
                LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 1000)
    public void test2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " -> 테스트 2 : " +
                LocalDateTime.now());
    }

//    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scraping scheduler is started");
                // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for(CompanyEntity company : companies){
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = yahooFinanceScrapper.scrap(
                    Company.builder()
                            .name(company.getName())
                            .ticker(company.getTicker())
                            .build()
            );

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
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