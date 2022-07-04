package com.test.demo.config;

import com.test.demo.utils.CustomDSList;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.stream.Collectors;

public class CustomRoutingDS extends AbstractRoutingDataSource {

    private CustomDSList<String> customDSList;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDS) {
        super.setTargetDataSources(targetDS);

        customDSList = new CustomDSList<>(
                targetDS.keySet().stream().filter(key -> key.toString().contains("slave"))
                        .map(key -> key.toString()).collect(Collectors.toList()));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            return customDSList.getDS();
        } else {
            return "master";
        }
    }
}

