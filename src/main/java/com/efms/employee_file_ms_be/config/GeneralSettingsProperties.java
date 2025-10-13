package com.efms.employee_file_ms_be.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "efms.general-settings")
@Getter
@Setter
public class GeneralSettingsProperties {

    private Integer workingDaysPerMonth;

    private BigDecimal seniorityIncreasePercentage;

    private BigDecimal contributionAfpPercentage;
}
