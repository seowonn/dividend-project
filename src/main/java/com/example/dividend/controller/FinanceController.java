package com.example.dividend.controller;

import com.example.dividend.model.ScrapedResult;
import com.example.dividend.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(
            @PathVariable(name = "companyName") String companyName
    ){
        ScrapedResult dividendByCompanyName =
                financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(dividendByCompanyName);
    }
}
