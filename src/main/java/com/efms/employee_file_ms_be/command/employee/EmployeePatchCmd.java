package com.efms.employee_file_ms_be.command.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeUpdateRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.Constants;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.EmployeeNotFoundException;
import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeRepository;
import com.efms.employee_file_ms_be.service.EmployeeHistoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@CommandExecute
@RequiredArgsConstructor
public class EmployeePatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private EmployeeUpdateRequest employeeUpdateRequest;

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

        Map<String, FieldChange> changes = detectChanges(employee, employeeUpdateRequest);

        updateProperties(employee, employeeUpdateRequest);
        employee = repository.save(employee);
        employeeResponse = mapper.toDTO(employee);

        if (!changes.isEmpty()) {
            historyService.saveEmployeeHistoryAsync(
                    employee,
                    ChangeType.UPDATE,
                    userName,
                    buildChangeSummary(changes),
                    changes
            );
        }
    }

    private void updateProperties(Employee employee, EmployeeUpdateRequest employeeUpdateRequest) {
        Optional.ofNullable(employeeUpdateRequest.getFirstName()).ifPresent(employee::setFirstName);
        Optional.ofNullable(employeeUpdateRequest.getLastName()).ifPresent(employee::setLastName);
        Optional.ofNullable(employeeUpdateRequest.getCi()).ifPresent(employee::setCi);
        Optional.ofNullable(employeeUpdateRequest.getEmail()).ifPresent(employee::setEmail);
        Optional.ofNullable(employeeUpdateRequest.getPhone()).ifPresent(employee::setPhone);
        Optional.ofNullable(employeeUpdateRequest.getAddress()).ifPresent(employee::setAddress);
        Optional.ofNullable(employeeUpdateRequest.getBirthDate()).ifPresent(employee::setBirthDate);
        Optional.ofNullable(employeeUpdateRequest.getHireDate()).ifPresent(employee::setHireDate);
        Optional.ofNullable(employeeUpdateRequest.getStatus()).ifPresent(
                status -> employee.setStatus(EmployeeStatus.valueOf(status))
        );
        Optional.ofNullable(employeeUpdateRequest.getEmergencyContact()).ifPresent(employee::setEmergencyContact);
        Optional.ofNullable(employeeUpdateRequest.getPositionId()).ifPresent(positionId -> {
            Position position = new Position();
            position.setId(UUID.fromString(positionId));
            employee.setPosition(position);
        });
        Optional.ofNullable(employeeUpdateRequest.getType()).ifPresent(employee::setType);
        Optional.ofNullable(employeeUpdateRequest.getBranchId()).ifPresent(branchId -> {
            Branch branch = new Branch();
            branch.setId(UUID.fromString(branchId));
            employee.setBranch(branch);
        });
        Optional.ofNullable(employeeUpdateRequest.getDisassociationDate()).ifPresent(employee::setDisassociationDate);
        Optional.ofNullable(employeeUpdateRequest.getDisassociationReason()).ifPresent(employee::setDisassociationReason);
        Optional.ofNullable(employeeUpdateRequest.getContractCompany()).ifPresent(employee::setContractCompany);
        Optional.ofNullable(employeeUpdateRequest.getContractPosition()).ifPresent(employee::setContractPosition);
    }

    private Map<String, FieldChange> detectChanges(Employee employee, EmployeeUpdateRequest request) {
        Map<String, FieldChange> changes = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        if (request.getFirstName() != null && !request.getFirstName().equals(employee.getFirstName())) {
            changes.put("firstName", createFieldChange("firstName", employee.getFirstName(), request.getFirstName(), now));
        }

        if (request.getLastName() != null && !request.getLastName().equals(employee.getLastName())) {
            changes.put("lastName", createFieldChange("lastName", employee.getLastName(), request.getLastName(), now));
        }

        if (request.getCi() != null && !request.getCi().equals(employee.getCi())) {
            changes.put("ci", createFieldChange("ci", employee.getCi(), request.getCi(), now));
        }

        if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail())) {
            changes.put("email", createFieldChange("email", employee.getEmail(), request.getEmail(), now));
        }

        if (request.getPhone() != null && !Objects.equals(request.getPhone(), employee.getPhone())) {
            changes.put("phone", createFieldChange("phone", employee.getPhone(), request.getPhone(), now));
        }

        if (request.getAddress() != null && !Objects.equals(request.getAddress(), employee.getAddress())) {
            changes.put("address", createFieldChange("address", employee.getAddress(), request.getAddress(), now));
        }

        if (request.getBirthDate() != null && !Objects.equals(request.getBirthDate(), employee.getBirthDate())) {
            changes.put("birthDate", createFieldChange("birthDate", employee.getBirthDate(), request.getBirthDate(), now));
        }

        if (request.getHireDate() != null && !Objects.equals(request.getHireDate(), employee.getHireDate())) {
            changes.put("hireDate", createFieldChange("hireDate", employee.getHireDate(), request.getHireDate(), now));
        }

        if (request.getStatus() != null) {
            EmployeeStatus newStatus = EmployeeStatus.valueOf(request.getStatus());
            if (!newStatus.equals(employee.getStatus())) {
                changes.put("status", createFieldChange("status", employee.getStatus(), newStatus, now));
            }
        }

        if (request.getPositionId() != null) {
            String currentPositionId = employee.getPosition() != null ? employee.getPosition().getId().toString() : null;
            if (!request.getPositionId().equals(currentPositionId)) {
                changes.put("positionId", createFieldChange("positionId", currentPositionId, request.getPositionId(), now));
            }
        }

        if (request.getBranchId() != null) {
            String currentBranchId = employee.getBranch() != null ? employee.getBranch().getId().toString() : null;
            if (!request.getBranchId().equals(currentBranchId)) {
                changes.put("branchId", createFieldChange("branchId", currentBranchId, request.getBranchId(), now));
            }
        }

        if (request.getEmergencyContact() != null && !Objects.equals(request.getEmergencyContact(), employee.getEmergencyContact())) {
            changes.put("emergencyContact", createFieldChange("emergencyContact", employee.getEmergencyContact(), request.getEmergencyContact(), now));
        }

        if (request.getDisassociationDate() != null && !Objects.equals(request.getDisassociationDate(), employee.getDisassociationDate())) {
            changes.put("disassociationDate", createFieldChange("disassociationDate", employee.getDisassociationDate(), request.getDisassociationDate(), now));
        }

        if (request.getDisassociationReason() != null && !Objects.equals(request.getDisassociationReason(), employee.getDisassociationReason())) {
            changes.put("disassociationReason", createFieldChange("disassociationReason", employee.getDisassociationReason(), request.getDisassociationReason(), now));
        }

        if (request.getContractCompany() != null && !Objects.equals(request.getContractCompany(), employee.getContractCompany())) {
            changes.put("contractCompany", createFieldChange("contractCompany", employee.getContractCompany(), request.getContractCompany(), now));
        }

        if (request.getContractPosition() != null && !Objects.equals(request.getContractPosition(), employee.getContractPosition())) {
            changes.put("contractPosition", createFieldChange("contractPosition", employee.getContractPosition(), request.getContractPosition(), now));
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

    private String buildChangeSummary(Map<String, FieldChange> changes) {
        return Constants.HistoryEvents.FIELDS_UPDATE + String.join(", ", changes.keySet());
    }
}
