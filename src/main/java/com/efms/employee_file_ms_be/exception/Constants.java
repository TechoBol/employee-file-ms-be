package com.efms.employee_file_ms_be.exception;

/**
 * @author Josue Veliz
 */
public class Constants {

    public static class ExceptionMessage {
        public static final String ERROR_ACCESSING_FIELDS="Error accessing field %s.";
        public static final String EMPLOYEE_NOT_FOUND = "Employee with id {employeeId} not found.";
        public static final String END_DATE_BEFORE_DATE = "End date cannot be earlier than start date.";
        public static final String ABSENCE_NOT_FOUND = "Absence with id {absenceId} not found.";
        public static final String VACATIONS_FULL_DAY = "Multi-day vacations must be full-day.";
        public static final String RECORD_EDIT_NOT_ALLOWED = "The record cannot be edited. Editing is only allowed during the registration month or until the 15th day of the following month.";
        public static final String BASE_SALARY_NOT_FOUND = "Base salary with id {baseSalaryId} not found";
        public static final String BRANCH_NOT_FOUND = "Branch with id {branchId} not found";
        public static final String COMPANY_NOT_FOUND = "Company with id {companyId} not found";
        public static final String DEPARTMENT_NOT_FOUND = "Department with id {departmentId} not found";
        public static final String LOCATION_NOT_FOUND = "Location with id {locationId} not found";
        public static final String POSITON_NOT_FOUND = "Position with  id {positionId} not found";
        public static final String SALARY_EVENT_NOT_FOUND =  "Salary event with id {salaryId} not found";
        public static final String COMMON_BAD_REQUEST = "Bad Request: {message}";
        public static final String ABSENCE_EMPLOYEE_NOT_NULL = "The employee of the absence cannot be null";
        public static final String GENERAL_SETTINGS_NOT_FOUND = "General settings with id {settingsId} not found";
        public static final String VACATION_NOT_FOUND = "Vacation with id {vacationId} not found";
        public static final String MEMORANDUM_NOT_FOUND = "Memorandum with id {memorandumId} not found";
        public static final String ADVANCE_NOT_FOUND = "Advance with id {advanceId} not found";
        public static final String ADVANCE_EMPLOYEE_NOT_NULL = "The employee of the advance cannot be null";
        public static final String ADVANCE_PAYMENT_EXCEED = "The advance payment cannot exceed the base salary.";
        public static final String FILE_DELETE_EXCEPTION = "Error deleting file {uuidFileName}, error: {error}";
        public static final String FILE_EMPTY_EXCEPTION = "File {fileName} is empty";
        public static final String NO_FILES_PROVIDED_EXCEPTION = "At least one file must be provided.";
        public static final String FILE_SECTION_MISMATCH_EXCEPTION = "The number of files ({fileCount}) does not match the number of sections ({sectionCount}).";
        public static final String FILE_NOT_FOUND = "File of employee {employeeId} not found.";
    }
}
