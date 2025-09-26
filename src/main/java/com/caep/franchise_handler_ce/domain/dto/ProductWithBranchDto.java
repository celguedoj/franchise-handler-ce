package com.caep.franchise_handler_ce.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWithBranchDto {

    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private int stock;
}
