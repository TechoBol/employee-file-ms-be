package com.efms.employee_file_ms_be.controller.salary_event;

import com.efms.employee_file_ms_be.command.salary_event.SalaryEventDeleteByIdCmd;
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
@RequestMapping(Constants.Path.SALARY_EVENT_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.SALARY_EVENT)
public class SalaryEventDeleteController {

    private final SalaryEventDeleteByIdCmd command;

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Salary Event by id")
    public ResponseEntity<Void> deleteSalaryEvent(@PathVariable String id) {
        command.setId(id);
        command.execute();

        if (command.getResponse() > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
