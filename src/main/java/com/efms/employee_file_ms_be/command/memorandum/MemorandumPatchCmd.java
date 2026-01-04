package com.efms.employee_file_ms_be.command.memorandum;

import com.efms.employee_file_ms_be.api.request.MemorandumUpdateRequest;
import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.exception.MemorandumNotFoundException;
import com.efms.employee_file_ms_be.exception.RecordEditNotAllowedException;
import com.efms.employee_file_ms_be.model.domain.Memorandum;
import com.efms.employee_file_ms_be.model.mapper.MemorandumMapper;
import com.efms.employee_file_ms_be.model.repository.MemorandumRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.efms.employee_file_ms_be.command.Constants.MAX_DAYS_TO_EDIT;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class MemorandumPatchCmd implements Command {

    @Setter
    private String id;

    @Setter
    private MemorandumUpdateRequest memorandumUpdateRequest;

    @Getter
    private MemorandumResponse memorandumResponse;

    private final MemorandumRepository repository;

    private final MemorandumMapper mapper;

    @Override
    public void execute() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Memorandum memorandum = repository.findByIdAndCompanyId(UUID.fromString(id), companyId)
                .orElseThrow(() -> new MemorandumNotFoundException(id));

        validateEditingTimeframe(memorandum);

        updateProperties(memorandum, memorandumUpdateRequest);
        memorandum = repository.save(memorandum);
        memorandumResponse = mapper.toDTO(memorandum);
    }

    private void validateEditingTimeframe(Memorandum memorandum) {
        LocalDate now = LocalDate.now();
        LocalDate memorandumDate = memorandum.getMemorandumDate();

        LocalDate lastDayOfMemorandumMonth = memorandumDate.withDayOfMonth(memorandumDate.lengthOfMonth());

        LocalDate fifthDayOfNextMonth = lastDayOfMemorandumMonth.plusDays(MAX_DAYS_TO_EDIT);

        if (now.isAfter(fifthDayOfNextMonth)) {
            throw new RecordEditNotAllowedException();
        }
    }

    private void updateProperties(Memorandum memorandum, MemorandumUpdateRequest memorandumUpdateRequest) {
        Optional.ofNullable(memorandumUpdateRequest.getType()).ifPresent(memorandum::setType);
        Optional.ofNullable(memorandumUpdateRequest.getDescription()).ifPresent(memorandum::setDescription);
        Optional.ofNullable(memorandumUpdateRequest.getMemorandumDate()).ifPresent(memorandum::setMemorandumDate);
        Optional.ofNullable(memorandumUpdateRequest.getIsPositive()).ifPresent(memorandum::setIsPositive);
    }
}
