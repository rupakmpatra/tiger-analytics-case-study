package org.subrat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.subrat.dto.PricingFeedDto;
import org.subrat.response.ApiResponse;
import org.subrat.service.JobLauncherService;
import org.subrat.service.PricingFeedService;
import org.subrat.util.PricingFeedFilter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pricing-feed")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PricingFeedController {
    private final PricingFeedService pricingFeedService;
    private final JobLauncherService jobLauncherService;

    @GetMapping("/count")
    public ApiResponse totalCount() {
        Long count = pricingFeedService.totalCount();
        return ApiResponse.<Long>builder()
                .data(count)
                .status(HttpStatus.OK.toString())
                .message("Total record count")
                .build();
    }
    @GetMapping
    public ApiResponse findAll(@RequestParam(required = false) String storeId,
                               @RequestParam(required = false) String sku,
                               @RequestParam(required = false) String productName,
                               @RequestParam(required = false) BigDecimal price,
                               @RequestParam(required = false) LocalDateTime date,
                               Pageable pageable) {
        PricingFeedFilter filter = PricingFeedFilter.builder().storeId(storeId).sku(sku).productName(productName).price(price).date(date).build();
        Page<PricingFeedDto> response = pricingFeedService.findAll(filter, pageable);
        return ApiResponse.<List<PricingFeedDto>>builder()
                .data(response.getContent())
                .message("All Pricing Feed details")
                .status(HttpStatus.OK.toString())
                .total(response.stream().count())
                .totalPages(response.getTotalPages())
                .pageNum(response.getNumber())
                .build();
    }

    @GetMapping("/{storeId}/{sku}")
    public ApiResponse getPricingFeedById(@PathVariable String storeId, @PathVariable String sku) throws Exception {
        PricingFeedDto pricingFeedDto = pricingFeedService.pricingFeedById(storeId, sku);
        return ApiResponse.<PricingFeedDto>builder()
                .data(pricingFeedDto)
                .message("Pricing feed details")
                .status(HttpStatus.OK.toString())
                .total(1L).build();
    }

    @PostMapping
    public ApiResponse create(@RequestBody PricingFeedDto pricingFeedDto) {
        pricingFeedDto = pricingFeedService.save(pricingFeedDto);
        return ApiResponse.<PricingFeedDto>builder()
                .data(pricingFeedDto)
                .message("Pricing feed details created successfully")
                .status(HttpStatus.OK.toString())
                .total(1L).build();
    }

    @PutMapping("/{storeId}/{sku}")
    public ApiResponse update(@PathVariable String storeId, @PathVariable String sku, @RequestBody PricingFeedDto pricingFeedDto) throws Exception {
        PricingFeedDto pricingFeed = pricingFeedService.update(storeId, sku, pricingFeedDto);
        return ApiResponse.<PricingFeedDto>builder()
                .data(pricingFeedDto)
                .message("Pricing detail updated successfully")
                .status(HttpStatus.OK.toString())
                .total(1L)
                .build();
    }

    @DeleteMapping("/{storeId}/{sku}")
    public ApiResponse delete(@PathVariable String storeId, @PathVariable String sku) throws Exception {
        String message = pricingFeedService.deletePricingFeed(storeId, sku);
        return ApiResponse.<String>builder()
                .data(message)
                .message(message)
                .status(HttpStatus.OK.toString())
                .total(1L)
                .build();
    }
    @PostMapping("/import")
    public String importPricingFeed(@RequestParam("file") MultipartFile file) throws Exception {
        return jobLauncherService.lauchJob(file);
    }
}
