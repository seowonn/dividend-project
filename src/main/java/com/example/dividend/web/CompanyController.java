package com.example.dividend.web;

import com.example.dividend.exception.DividendException;
import com.example.dividend.model.Company;
import com.example.dividend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static com.example.dividend.model.type.ErrorCode.TICKER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("keyword") String keyword){
        return null;
    }

    @GetMapping
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addCompany(
            @RequestBody Company request
    ){
        String ticker = request.getTicker();
        if(ObjectUtils.isEmpty(ticker)){
            throw new DividendException(TICKER_NOT_FOUND);
        }
        Company company = companyService.save(ticker);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
