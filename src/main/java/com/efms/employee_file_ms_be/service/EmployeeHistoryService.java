package com.efms.employee_file_ms_be.service;

import com.efms.employee_file_ms_be.model.domain.*;
import com.efms.employee_file_ms_be.model.mapper.employee.EmployeeMapper;
import com.efms.employee_file_ms_be.model.repository.EmployeeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Josue Veliz
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeHistoryService {

    private final EmployeeHistoryRepository historyRepository;
    private final EmployeeMapper employeeMapper;

    @Async
    public CompletableFuture<Void> saveEmployeeHistoryAsync(
            Employee employee,
            ChangeType changeType,
            String user,
            String reason,
            Map<String, FieldChange> changes
    ) {
        try {
            EmployeeHistory history = new EmployeeHistory();
            history.setEmployeeId(employee.getId());
            history.setChangedAt(LocalDateTime.now());
            history.setChangedBy(Optional.ofNullable(user).orElse("Desconocido"));
            history.setChangeType(changeType);
            history.setSnapshot(createSnapshot(employee));
            history.setChanges(changes);
            history.setReason(reason);
            history.setCompanyId(employee.getCompanyId());

            historyRepository.save(history);

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Error saving employee history for employeeId: {}", employee.getId(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Void> saveEmployeeHistoryAsync(
            Employee employee,
            ChangeType changeType,
            String user,
            String reason
    ) {
        return saveEmployeeHistoryAsync(employee, changeType, user, reason, null);
    }

    @Async
    public CompletableFuture<Void> saveEmployeeHistoryBatchAsync(
            List<Employee> employees,
            ChangeType changeType,
            Function<Employee, String> reasonBuilder
    ) {
        try {
            List<EmployeeHistory> histories = employees.stream()
                    .map(employee -> {
                        EmployeeHistory history = new EmployeeHistory();
                        history.setEmployeeId(employee.getId());
                        history.setChangedAt(LocalDateTime.now());
                        history.setChangedBy("system");
                        history.setChangeType(changeType);
                        history.setSnapshot(createSnapshot(employee));
                        history.setReason(reasonBuilder.apply(employee));
                        history.setCompanyId(employee.getCompanyId());
                        return history;
                    })
                    .toList();

            historyRepository.saveAll(histories);
            log.info("Saved {} history records in batch", histories.size());

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Error saving employee history batch", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    public void saveEmployeeHistory(
            Employee employee,
            ChangeType changeType,
            String user,
            String reason,
            Map<String, FieldChange> changes
    ) {
        EmployeeHistory history = new EmployeeHistory();
        history.setEmployeeId(employee.getId());
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy(Optional.ofNullable(user).orElse("Desconocido"));
        history.setChangeType(changeType);
        history.setSnapshot(createSnapshot(employee));
        history.setChanges(changes);
        history.setReason(reason);
        history.setCompanyId(employee.getCompanyId());

        historyRepository.save(history);
    }

    public Page<EmployeeHistory> getEmployeeHistoryPaginated(
            UUID employeeId,
            UUID companyId,
            Pageable pageable
    ) {
        return historyRepository.findByEmployeeIdAndCompanyIdOrderByChangedAtDesc(
                employeeId,
                companyId,
                pageable
        );
    }

    private EmployeeSnapshot createSnapshot(Employee employee) {
        EmployeeSnapshot snapshot = new EmployeeSnapshot();
        snapshot.setId(employee.getId());
        snapshot.setFirstName(employee.getFirstName());
        snapshot.setLastName(employee.getLastName());
        snapshot.setCi(employee.getCi());
        snapshot.setEmail(employee.getEmail());
        snapshot.setPhone(employee.getPhone());
        snapshot.setAddress(employee.getAddress());
        snapshot.setBirthDate(employee.getBirthDate());
        snapshot.setHireDate(employee.getHireDate());
        snapshot.setStatus(employee.getStatus());
        snapshot.setType(employee.getType());
        snapshot.setEmergencyContact(employee.getEmergencyContact());
        snapshot.setIsDeleted(employee.getIsDeleted());
        snapshot.setDeletedAt(employee.getDeletedAt());
        snapshot.setIsDisassociated(employee.getIsDisassociated());
        snapshot.setDisassociatedAt(employee.getDisassociatedAt());
        snapshot.setDisassociationDate(employee.getDisassociationDate());
        snapshot.setDisassociationReason(employee.getDisassociationReason());

        if (employee.getBranch() != null) {
            snapshot.setBranchId(employee.getBranch().getId());
            snapshot.setBranchName(employee.getBranch().getName());
        }

        if (employee.getPosition() != null) {
            snapshot.setPositionId(employee.getPosition().getId());
            snapshot.setPositionName(employee.getPosition().getName());
        }

        return snapshot;
    }
}
