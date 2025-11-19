package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PayrollCalculateByPageableCmd implements Command {

    @Setter
    private Pageable pageable;

    @Setter
    private Integer period;

    @Getter
    private Page<PayrollEmployeeResponse> payrollPageResponse;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        Page<EmployeeResponse> employeesPage = findEmployees();

        List<PayrollEmployeeResponse> payrollResponses = employeesPage.getContent().stream()
                .map(employee -> {
                    PayrollResponse payroll = calculatePayroll(employee.getId().toString());
                    PayrollEmployeeResponse response = new PayrollEmployeeResponse();
                    response.setEmployee(employee);
                    response.setPayroll(payroll);
                    return response;
                })
                .toList();

        payrollPageResponse = new PageImpl<>(
                payrollResponses,
                pageable,
                employeesPage.getTotalElements()
        );
    }

    private Page<EmployeeResponse> findEmployees() {
        EmployeeReadByPageableCmd command = commandFactory.createCommand(EmployeeReadByPageableCmd.class);
        command.setPageable(pageable);
        command.execute();
        return command.getEmployees();
    }

    private PayrollResponse calculatePayroll(String employeeId) {
        PayrollCalculateByEmployeeIdCmd command = commandFactory.createCommand(PayrollCalculateByEmployeeIdCmd.class);
        command.setEmployeeId(employeeId);
        if(period != null) {
            command.setPeriod(period);
        }
        command.execute();
        return command.getPayrollResponse();
    }
}
