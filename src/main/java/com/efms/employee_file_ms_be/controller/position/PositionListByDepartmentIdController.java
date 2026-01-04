package com.efms.employee_file_ms_be.controller.position;

import com.efms.employee_file_ms_be.api.response.PositionResponse;
import com.efms.employee_file_ms_be.command.position.PositionListByDepartmentIdCmd;
import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Josue Veliz
 */
@RestController
@RequestMapping(Constants.Path.POSITION_PATH)
@RequiredArgsConstructor
@Tag(name = Constants.Tag.POSITION)
public class PositionListByDepartmentIdController {

    private final PositionListByDepartmentIdCmd command;

    @GetMapping("/departments/{departmentId}")
    @Operation(summary = "Get positions by company ID")
    public ResponseEntity<List<PositionResponse>> getByCompanyAndDepartmentId(@PathVariable("departmentId") String departmentId) {
        command.setDepartmentId(departmentId);
        command.execute();

        return ResponseEntity.ok(command.getPositions());
    }
}
