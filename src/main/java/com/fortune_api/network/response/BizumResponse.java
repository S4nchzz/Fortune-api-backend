package com.fortune_api.network.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BizumResponse {
    private Date date;
    private String from;
    private double amount;
    private String description;
    private boolean amountIn;
}
