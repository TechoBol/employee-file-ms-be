package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeHistoryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.EmployeeHistory;
import com.efms.employee_file_ms_be.model.mapper.EmployeeHistoryMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeHistoryRepository;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
@Slf4j
public class EmployeeHistoryListCmd implements Command {

    @Setter
    private String employeeId;

    @Setter
    private Integer page = 0;

    @Setter
    private Integer size = 20;

    @Setter
    private String sortBy = "changedAt";

    @Setter
    private String sortDirection = "DESC";

    @Getter
    private Page<EmployeeHistoryResponse> historyPage;

    private final EmployeeHistoryRepository historyRepository;
    private final EmployeeHistoryMapper historyMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        UUID employeeUuid = UUID.fromString(employeeId);

        log.info("Fetching history for employee: {} in company: {}", employeeId, companyId);

        employeeRepository.findByIdAndCompanyId(employeeUuid, companyId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sortBy
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeHistory> historyEntityPage = historyRepository
                .findByEmployeeIdAndCompanyIdOrderByChangedAtDesc(
                        employeeUuid,
                        companyId,
                        pageable
                );

        List<EmployeeHistoryResponse> historyResponses = historyMapper.toDTOList(
                historyEntityPage.getContent()
        );

        historyPage = new PageImpl<>(
                historyResponses,
                pageable,
                historyEntityPage.getTotalElements()
        );

        log.info("Found {} history records for employee: {}",
                historyPage.getTotalElements(), employeeId);
    }
}
