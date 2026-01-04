package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.ChangeType;
import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.domain.FieldChange;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.service.EmployeeHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class EmployeeAssociateCmd implements Command {

    @Setter
    private String id;

    @Setter
    private String userName;

    @Getter
    private EmployeeResponse employeeResponse;

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    private final EmployeeHistoryService historyService;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Employee employee = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        LocalDateTime previousDisassociatedAt = employee.getDisassociatedAt();
        LocalDate previousDisassociationDate = employee.getDisassociationDate();
        String previousDisassociationReason = employee.getDisassociationReason();
        EmployeeStatus previousStatus = employee.getStatus();

        employee.setIsDisassociated(false);
        employee.setDisassociatedAt(null);
        employee.setDisassociationDate(null);
        employee.setDisassociationReason(null);
        employee.setStatus(EmployeeStatus.ACTIVE);

        employee = repository.save(employee);
        employeeResponse = mapper.toDTO(employee);

        saveAssociationHistory(employee, previousDisassociatedAt, previousDisassociationDate,
                previousDisassociationReason, previousStatus);
    }

    private void saveAssociationHistory(Employee employee,
                                        LocalDateTime previousDisassociatedAt,
                                        LocalDate previousDisassociationDate,
                                        String previousDisassociationReason,
                                        EmployeeStatus previousStatus) {

        String timeDisassociated = calculateDisassociationPeriod(previousDisassociatedAt);

        String reason = String.format(
                "Empleado reasociado/reactivado. " +
                        "Periodo de desvinculación: %s. " +
                        "Estado anterior: %s. " +
                        "Fecha de desvinculación: %s. " +
                        "Razón de desvinculación anterior: %s",
                timeDisassociated,
                previousStatus,
                previousDisassociationDate != null ? previousDisassociationDate.toString() : "N/A",
                previousDisassociationReason != null ? previousDisassociationReason : "N/A"
        );

        Map<String, FieldChange> changes = buildAssociationChanges(
                previousDisassociatedAt,
                previousDisassociationDate,
                previousDisassociationReason,
                previousStatus
        );

        historyService.saveEmployeeHistoryAsync(
                employee,
                ChangeType.ASSOCIATE,
                userName,
                reason,
                changes
        );
    }

    private Map<String, FieldChange> buildAssociationChanges(
            LocalDateTime previousDisassociatedAt,
            LocalDate previousDisassociationDate,
            String previousDisassociationReason,
            EmployeeStatus previousStatus) {

        Map<String, FieldChange> changes = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        changes.put("isDisassociated", createFieldChange(
                "isDisassociated", true, false, now
        ));

        if (previousDisassociatedAt != null) {
            changes.put("disassociatedAt", createFieldChange(
                    "disassociatedAt", previousDisassociatedAt, null, now
            ));
        }

        if (previousDisassociationDate != null) {
            changes.put("disassociationDate", createFieldChange(
                    "disassociationDate", previousDisassociationDate, null, now
            ));
        }

        if (previousDisassociationReason != null) {
            changes.put("disassociationReason", createFieldChange(
                    "disassociationReason", previousDisassociationReason, null, now
            ));
        }

        if (previousStatus != EmployeeStatus.ACTIVE) {
            changes.put("status", createFieldChange(
                    "status", previousStatus, EmployeeStatus.ACTIVE, now
            ));
        }

        return changes;
    }

    private FieldChange createFieldChange(String fieldName, Object oldValue, Object newValue, LocalDateTime changedAt) {
        FieldChange fieldChange = new FieldChange();
        fieldChange.setFieldName(fieldName);
        fieldChange.setOldValue(oldValue);
        fieldChange.setNewValue(newValue);
        fieldChange.setChangedAt(changedAt);
        return fieldChange;
    }

    private String calculateDisassociationPeriod(LocalDateTime previousDisassociatedAt) {
        if (previousDisassociatedAt == null) {
            return "Periodo desconocido";
        }

        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(previousDisassociatedAt, now);
        long months = ChronoUnit.MONTHS.between(previousDisassociatedAt, now);
        long years = ChronoUnit.YEARS.between(previousDisassociatedAt, now);

        if (years > 0) {
            long remainingMonths = months % 12;
            return String.format("%d año(s) y %d mes(es)", years, remainingMonths);
        } else if (months > 0) {
            long remainingDays = days % 30;
            return String.format("%d mes(es) y %d día(s)", months, remainingDays);
        } else {
            return String.format("%d día(s)", days);
        }
    }
}
