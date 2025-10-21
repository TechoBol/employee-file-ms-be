package com.efms.employee_file_ms_be.controller.memorandum;

import com.efms.employee_file_ms_be.api.request.MemorandumCreateRequest;
import com.efms.employee_file_ms_be.api.response.MemorandumResponse;
import com.efms.employee_file_ms_be.command.memorandum.MemorandumCreateCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.MEMORANDUM_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.MEMORANDUM)
public class MemorandumCreateController {

    private final MemorandumCreateCmd command;

    @PostMapping
    @Operation(summary = "Create a new Memorandum associated an user")
    public ResponseEntity<MemorandumResponse> createMemorandum(@Valid @RequestBody MemorandumCreateRequest request) {
        command.setMemorandumCreateRequest(request);
        command.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(command.getMemorandumResponse());
    }
}
