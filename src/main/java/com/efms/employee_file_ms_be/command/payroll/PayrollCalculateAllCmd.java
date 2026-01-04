package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollEmployeeResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollResponse;
import com.efms.employee_file_ms_be.api.response.payroll.PayrollSummaryResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadAllCmd;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class PayrollCalculateAllCmd implements Command {

    @Setter
    private Integer period;

    @Getter
    private PayrollSummaryResponse payrollSummary;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        List<EmployeeResponse> allEmployees = findAllEmployees();

        List<PayrollEmployeeResponse> payrollResponses = allEmployees.stream()
                .map(employee -> {
                    PayrollResponse payroll = calculatePayroll(employee.getId().toString());
                    PayrollEmployeeResponse response = new PayrollEmployeeResponse();
                    response.setEmployee(employee);
                    response.setPayroll(payroll);
                    return response;
                })
                .toList();

        payrollSummary = PayrollSummaryResponse.from(payrollResponses);
    }

    private List<EmployeeResponse> findAllEmployees() {
        EmployeeReadAllCmd command = commandFactory.createCommand(EmployeeReadAllCmd.class);
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
