package com.efms.employee_file_ms_be.command.payroll;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.api.response.payroll.*;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadAllCmd;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@CommandExecute
@RequiredArgsConstructor
public class PayrollCalculateByBranchCmd implements Command {

    @Setter
    private Integer period;

    @Getter
    private List<PayrollByBranchResponse> payrollByBranch;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        List<EmployeeResponse> allEmployees = findAllEmployees();

        Map<UUID, List<EmployeeResponse>> employeesByBranch = allEmployees.stream()
                .collect(Collectors.groupingBy(
                        employee -> UUID.fromString(employee.getBranchId() != null ? employee.getBranchId() : null)
                ));

        payrollByBranch = employeesByBranch.entrySet().stream()
                .map(entry -> {
                    UUID branchId = entry.getKey();
                    List<EmployeeResponse> employees = entry.getValue();

                    List<PayrollEmployeeResponse> payrollResponses = employees.stream()
                            .map(employee -> {
                                PayrollResponse payroll = calculatePayroll(employee.getId());
                                PayrollEmployeeResponse response = new PayrollEmployeeResponse();
                                response.setEmployee(employee);
                                response.setPayroll(payroll);
                                return response;
                            })
                            .toList();

                    PayrollByBranchResponse branchResponse = new PayrollByBranchResponse();
                    branchResponse.setBranchId(branchId);

                    if (!employees.isEmpty() && employees.getFirst().getBranchName() != null) {
                        branchResponse.setBranchName(employees.getFirst().getBranchName());
                    } else {
                        branchResponse.setBranchName("Sin Sucursal");
                    }

                    branchResponse.setEmployeeCount(employees.size());
                    branchResponse.setPayrolls(payrollResponses);

                    branchResponse.setTotals(calculateBranchTotals(payrollResponses));

                    return branchResponse;
                })
                .sorted(Comparator.comparing(
                        PayrollByBranchResponse::getBranchName,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .toList();
    }

    private List<EmployeeResponse> findAllEmployees() {
        EmployeeReadAllCmd command = commandFactory.createCommand(EmployeeReadAllCmd.class);
        command.execute();
        return command.getEmployees();
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

    private PayrollSummaryResponse.PayrollTotals calculateBranchTotals(List<PayrollEmployeeResponse> payrolls) {
        PayrollSummaryResponse.PayrollTotals totals = new PayrollSummaryResponse.PayrollTotals();
        totals.setDeductions(new HashMap<>());

        for (PayrollEmployeeResponse payrollEmployee : payrolls) {
            PayrollResponse payroll = payrollEmployee.getPayroll();
            if (payroll != null) {
                totals.setTotalSeniorityBonuses(addNullSafe(totals.getTotalSeniorityBonuses(), payroll.getSeniorityBonus()));
                totals.setTotalOtherBonuses(addNullSafe(totals.getTotalOtherBonuses(), payroll.getOtherBonuses()));
                totals.setTotalBonuses(addNullSafe(totals.getTotalBonuses(), payroll.getTotalBonuses()));
                totals.setTotalEarnings(addNullSafe(totals.getTotalEarnings(), payroll.getTotalEarnings()));
                totals.setTotalAfpDeductions(addNullSafe(totals.getTotalAfpDeductions(), payroll.getDeductionAfp()));
                totals.setTotalDeductions(addNullSafe(totals.getTotalDeductions(), payroll.getTotalDeductions()));
                totals.setNetAmount(addNullSafe(totals.getNetAmount(), payroll.getNetAmount()));

                if (payroll.getDeductions() != null) {
                    for (PayrollDeductionResponse deduction : payroll.getDeductions()) {
                        String type = deduction.getType();
                        BigDecimal amount = deduction.getTotalDeduction();
                        totals.getDeductions().put(type,
                                addNullSafe(totals.getDeductions().get(type), amount));
                    }
                }
            }
        }

        return totals;
    }

    private static BigDecimal addNullSafe(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a)
                .add(b == null ? BigDecimal.ZERO : b);
    }
}
