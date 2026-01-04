package com.efms.employee_file_ms_be.controller.employee;

import com.efms.employee_file_ms_be.api.request.EmployeeSearchRequest;
import com.efms.employee_file_ms_be.api.response.EmployeeResponse;
import com.efms.employee_file_ms_be.command.employee.EmployeeReadByPageableCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.EMPLOYEE_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.EMPLOYEE)
public class EmployeeReadByPageableController {

    private final EmployeeReadByPageableCmd command;

    @GetMapping
    @Operation(summary = "Get employees by company with optional filters and pagination")
    public ResponseEntity<Page<EmployeeResponse>> getByCompanyPageable(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ci,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) EmployeeType type,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) Boolean isDisassociated,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID positionId,
            Pageable pageable
    ) {
        EmployeeSearchRequest searchRequest = EmployeeSearchRequest.builder()
                .search(search)
                .ci(ci)
                .email(email)
                .phone(phone)
                .type(type)
                .status(status)
                .isDisassociated(isDisassociated)
                .branchId(branchId)
                .positionId(positionId)
                .pageable(pageable)
                .build();

        command.setSearchRequest(searchRequest);
        command.setPageable(pageable);
        command.execute();

        return ResponseEntity.ok(command.getEmployees());
    }
}
