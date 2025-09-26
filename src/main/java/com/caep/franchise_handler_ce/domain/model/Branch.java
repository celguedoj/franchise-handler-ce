package com.caep.franchise_handler_ce.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    private String id;
    private String name;
    @Builder.Default
    private List<Product> products = new ArrayList<>();
}
