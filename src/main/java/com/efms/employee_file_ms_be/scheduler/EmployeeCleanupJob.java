package com.efms.employee_file_ms_be.scheduler;

import com.efms.employee_file_ms_be.command.core.CommandFactory;
import com.efms.employee_file_ms_be.command.employee.EmployeeDeleteAllDisassociateCmd;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Josue Veliz
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeCleanupJob {

    private final CommandFactory commandFactory;

    @Scheduled(cron = "0 0 0 * * ?")
    public void autoDeleteDisassociatedEmployees() {
        log.info("Starting scheduled job: autoDeleteDisassociatedEmployees");

        try {
            EmployeeDeleteAllDisassociateCmd command = commandFactory.createCommand(EmployeeDeleteAllDisassociateCmd.class);
            command.execute();

            List<Map<String, String>> employeesInfo = command.getEmployeesInfo();

            log.info("Scheduled job completed successfully. Total employees marked as deleted: {}", employeesInfo.size());

            if (!employeesInfo.isEmpty()) {
                log.debug("Deleted employees details: {}", employeesInfo);
            }
        } catch (Exception e) {
            log.error("Error executing autoDeleteDisassociatedEmployees job", e);
        }
    }
}
