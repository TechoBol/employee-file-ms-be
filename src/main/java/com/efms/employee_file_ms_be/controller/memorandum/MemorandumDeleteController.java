package com.efms.employee_file_ms_be.controller.memorandum;

import com.efms.employee_file_ms_be.command.memorandum.MemorandumDeleteCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.MEMORANDUM_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.MEMORANDUM)
public class MemorandumDeleteController {

    private final MemorandumDeleteCmd command;

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Memorandum by id")
    public ResponseEntity<Void> deleteMemorandum(@PathVariable String id) {
        command.setId(id);
        command.execute();

        if (command.getResponse() > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
