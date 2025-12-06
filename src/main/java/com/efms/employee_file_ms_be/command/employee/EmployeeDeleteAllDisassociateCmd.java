package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.CompanyResponse;
import com.efms.employee_file_ms_be.command.company.CompanyListCmd;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
@Slf4j
public class EmployeeDeleteAllDisassociateCmd implements Command {

    @Getter
    private List<Map<String, String>> employeesInfo;

    private final EmployeeRepository employeeRepository;

    private final CommandFactory commandFactory;

    @Override
    public void execute() {
        log.info("Starting deletion process for all disassociated employees");
        employeesInfo = new ArrayList<>();
        List<CompanyResponse> allCompanies = findAllCompanies();

        LocalDateTime now = LocalDateTime.now();

        for (CompanyResponse company : allCompanies) {
            log.debug("Processing disassociated employees for company: {}", company.getId());
            processDisassociatedEmployees(UUID.fromString(company.getId()), now);
        }

        log.info("Deletion process completed. Total employees marked as deleted: {}", employeesInfo.size());
    }

    private void processDisassociatedEmployees(UUID companyId, LocalDateTime now) {
        LocalDateTime deletionThreshold = now.minusDays(30);

        Pageable pageable = PageRequest.of(0, 100);
        Page<Employee> employeesPage;
        int totalProcessed = 0;

        do {
            employeesPage = employeeRepository.findAllByCompanyIdAndIsDisassociatedAndDisassociatedAtBefore(
                    companyId,
                    true,
                    deletionThreshold,
                    pageable
            );

            if (!employeesPage.isEmpty()) {
                List<Employee> employees = getEmployees(companyId, now, employeesPage);

                employeeRepository.saveAll(employees);
                totalProcessed += employees.size();

                log.debug("Processed batch of {} employees for company: {}", employees.size(), companyId);
            }

            pageable = employeesPage.nextPageable();
        } while (employeesPage.hasNext());

        if (totalProcessed > 0) {
            log.info("Company {}: {} disassociated employees marked as deleted", companyId, totalProcessed);
        }
    }

    private List<Employee> getEmployees(UUID companyId, LocalDateTime now, Page<Employee> employeesPage) {
        List<Employee> employees = employeesPage.getContent();

        employees.forEach(employee -> {
            employee.setIsDeleted(true);
            employee.setDeletedAt(now);

            Map<String, String> info = new HashMap<>();
            info.put("id", employee.getId().toString());
            info.put("firstName", employee.getFirstName());
            info.put("lastName", employee.getLastName());
            info.put("companyId", companyId.toString());
            employeesInfo.add(info);
        });
        return employees;
    }

    private List<CompanyResponse> findAllCompanies() {
        CompanyListCmd companyListCmd = commandFactory.createCommand(CompanyListCmd.class);
        companyListCmd.execute();
        return companyListCmd.getCompanies();
    }
}