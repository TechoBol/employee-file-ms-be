package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.payroll.PayrollEmployeeResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeProjectionReadByPageableCmd;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadByPageableCmd;
import com.efms.employee_file_ms_be.model.repository.projection.EmployeeProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@CommandExecute
@RequiredArgsConstructor
@Slf4j
public class PayrollCalculateByCompanyCmd implements Command {

    @Setter
    private UUID companyId;

    @Setter
    private Pageable pageable;

    @Setter
    private Integer period;

    @Getter
    private Page<PayrollEmployeeResponse> payrollPageResponse;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        if (pageable == null) {
            pageable = PageRequest.of(0, 20);
        }

        log.debug("Calculating payroll for company {} - Page: {}, Size: {}",
                companyId, pageable.getPageNumber(), pageable.getPageSize());

        Page<EmployeeProjection> employeesPage = findEmployeesByCompany();

        if (employeesPage.isEmpty()) {
            log.debug("No employees found for company {}", companyId);
            payrollPageResponse = Page.empty(pageable);
            return;
        }

        log.debug("Found {} employees for company {} in page {}",
                employeesPage.getNumberOfElements(), companyId, pageable.getPageNumber());

        List<PayrollEmployeeResponse> payrollResponses = employeesPage.getContent().stream()
                .map(employee -> {
                    try {
                        PayrollResponse payroll = calculatePayroll(employee.getId().toString());
                        PayrollEmployeeResponse response = new PayrollEmployeeResponse();
                        response.setEmployee(employee);
                        response.setPayroll(payroll);
                        return response;
                    } catch (Exception e) {
                        log.error("Error calculating payroll for employee {}: {}",
                                employee.getId(), e.getMessage(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        payrollPageResponse = new PageImpl<>(
                payrollResponses,
                pageable,
                employeesPage.getTotalElements()
        );

        log.debug("Successfully calculated payroll for {} out of {} employees in page",
                payrollResponses.size(), employeesPage.getNumberOfElements());
    }

    private Page<EmployeeProjection> findEmployeesByCompany() {
        EmployeeProjectionReadByPageableCmd command = commandFactory.createCommand(EmployeeProjectionReadByPageableCmd.class);
        command.setPageable(pageable);
        command.execute();
        return command.getEmployeeProjectionPage();
    }

    private PayrollResponse calculatePayroll(String employeeId) {
        PayrollCalculateByEmployeeIdCmd command = commandFactory.createCommand(PayrollCalculateByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        if (period != null) {
            command.setPeriod(period);
        }
        command.execute();
        return command.getPayrollResponse();
    }
}
