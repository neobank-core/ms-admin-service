package com.neobank.adminservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {
    private long totalUsers;
    private long activeAccounts;
    private double todayTransactionVolume;
    private long todayTransactionCount;
    private long failedTransactionCount;
}
