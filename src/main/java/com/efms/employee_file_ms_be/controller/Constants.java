package com.efms.employee_file_ms_be.controller;

/**
 * @author Josue Veliz
 */
public class Constants {
    public static class Path {
        public static final String BASE_PATH = "/api";

        public static final String ABSENCES_PATH = BASE_PATH + "/absences";

        public static final String BASE_SALARY_PATH = BASE_PATH + "/base-salaries";

        public static final String BRANCH_PATH = BASE_PATH + "/branches";

        public static final String COMPANY_PATH = BASE_PATH + "/companies";

        public static final String DEPARTMENT_PATH = BASE_PATH + "/departments";

        public static final String EMPLOYEE_PATH = BASE_PATH + "/employees";

        public static final String GENERAL_SETTINGS_PATH = BASE_PATH + "/general-settings";

        public static final String PAYROLL_PATH = BASE_PATH + "/payrolls";

        public static final String POSITION_PATH = BASE_PATH + "/positions";

        public static final String SALARY_EVENT_PATH = BASE_PATH + "/salary-events";
    }

    public static class Tag {
        public static final String ABSENCE = "Absence";

        public static final String BASE_SALARY = "Base Salary";

        public static final String BRANCH = "Branch";

        public static final String COMPANY = "Company";

        public static final String DEPARTMENT = "Department";

        public static final String EMPLOYEE = "Employee";

        public static final String GENERAL_SETTINGS = "General Settings";

        public static final String LOCATION = "Location";

        public static final String PAYROLL = "Payroll";

        public static final String POSITION = "Position";

        public static final String SALARY_EVENT = "Salary Event";
    }
}
