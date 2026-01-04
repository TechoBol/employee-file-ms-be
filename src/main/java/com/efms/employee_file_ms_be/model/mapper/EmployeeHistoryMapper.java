package com.efms.employee_file_ms_be.model.mapper;

import com.efms.employee_file_ms_be.api.response.EmployeeHistoryResponse;
import com.efms.employee_file_ms_be.api.response.FieldChangeResponse;
import com.efms.employee_file_ms_be.model.domain.EmployeeHistory;
import com.efms.employee_file_ms_be.model.domain.FieldChange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Josue Veliz
 */
@Component
@RequiredArgsConstructor
public class EmployeeHistoryMapper {

    public EmployeeHistoryResponse toDTO(EmployeeHistory employeeHistory) {
        return EmployeeHistoryResponse.builder()
                .id(employeeHistory.getId() != null ? employeeHistory.getId().toString() : null)
                .employeeId(employeeHistory.getEmployeeId() != null ? employeeHistory.getEmployeeId().toString() : null)
                .changedAt(employeeHistory.getChangedAt())
                .changedBy(employeeHistory.getChangedBy())
                .changeType(employeeHistory.getChangeType() != null ? employeeHistory.getChangeType().name() : null)
                .reason(employeeHistory.getReason())
                .snapshot(employeeHistory.getSnapshot())
                .changes(mapChanges(employeeHistory.getChanges()))
                .companyId(employeeHistory.getCompanyId() != null ? employeeHistory.getCompanyId().toString() : null)
                .build();
    }

    public List<EmployeeHistoryResponse> toDTOList(List<EmployeeHistory> employeeHistories) {
        return employeeHistories.stream()
                .map(this::toDTO)
                .toList();
    }

    private Map<String, FieldChangeResponse> mapChanges(Map<String, FieldChange> changes) {
        if (changes == null || changes.isEmpty()) {
            return null;
        }

        return changes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> mapFieldChange(entry.getValue())
                ));
    }

    private FieldChangeResponse mapFieldChange(FieldChange fieldChange) {
        return FieldChangeResponse.builder()
                .fieldName(fieldChange.getFieldName())
                .oldValue(fieldChange.getOldValue())
                .newValue(fieldChange.getNewValue())
                .changedAt(fieldChange.getChangedAt())
                .displayName(getFieldDisplayName(fieldChange.getFieldName()))
                .build();
    }

    private String getFieldDisplayName(String fieldName) {
        return switch (fieldName) {
            case "firstName" -> "Nombre";
            case "lastName" -> "Apellido";
            case "ci" -> "CI/Documento";
            case "email" -> "Correo Electrónico";
            case "phone" -> "Teléfono";
            case "address" -> "Dirección";
            case "birthDate" -> "Fecha de Nacimiento";
            case "hireDate" -> "Fecha de Contratación";
            case "status" -> "Estado";
            case "type" -> "Tipo";
            case "positionId" -> "Cargo";
            case "branchId" -> "Sucursal";
            case "emergencyContact" -> "Contacto de Emergencia";
            case "isDisassociated" -> "Desvinculado";
            case "disassociatedAt" -> "Fecha/Hora de Desvinculación";
            case "disassociationDate" -> "Fecha de Desvinculación";
            case "disassociationReason" -> "Razón de Desvinculación";
            case "isDeleted" -> "Eliminado";
            case "deletedAt" -> "Fecha/Hora de Eliminación";
            default -> fieldName;
        };
    }
}
