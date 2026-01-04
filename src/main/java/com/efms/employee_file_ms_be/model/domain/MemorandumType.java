package com.efms.employee_file_ms_be.model.domain;

import com.efms.employee_file_ms_be.model.Constants;
import lombok.Getter;

@Getter
public enum MemorandumType {
    VERBAL_WARNING(Constants.MemorandumType.VERBAL_WARNING),
    WRITTEN_WARNING(Constants.MemorandumType.WRITTEN_WARNING),
    SUSPENSION(Constants.MemorandumType.SUSPENSION),
    CALL_TO_ATTENTION(Constants.MemorandumType.CALL_TO_ATTENTION),
    RECOGNITION(Constants.MemorandumType.RECOGNITION),
    CONGRATULATION(Constants.MemorandumType.CONGRATULATION),
    PERFORMANCE_BONUS(Constants.MemorandumType.PERFORMANCE_BONUS),
    OTHER(Constants.MemorandumType.OTHER);

    private final String displayName;

    MemorandumType(String displayName) {
        this.displayName = displayName;
    }
}
