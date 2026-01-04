package com.efms.employee_file_ms_be.command.memorandum;

import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.core.Command;
import com.efms.employee_file_ms_be.command.core.CommandExecute;
import com.efms.employee_file_ms_be.config.TenantContext;
import com.efms.employee_file_ms_be.model.domain.Memorandum;
import com.efms.employee_file_ms_be.model.mapper.MemorandumMapper;
import com.efms.employee_file_ms_be.model.repository.MemorandumRepository;
import com.efms.employee_file_ms_be.util.DateUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
@CommandExecute
@RequiredArgsConstructor
public class MemorandumListCmd implements Command {

    @Setter
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @Getter
    private List<MemorandumResponse> memorandumResponseList;

    @Getter
    private List<Memorandum> memorandumList;

    private final MemorandumRepository repository;

    private final MemorandumMapper mapper;

    @Override
    public void execute() {
        startDate = DateUtils.getStartDateOrDefault(startDate);
        endDate = DateUtils.getEndDateOrDefault(endDate);
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        memorandumList = repository.findByCompanyInDateRange(companyId, startDate, endDate);
        memorandumResponseList = memorandumList.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
