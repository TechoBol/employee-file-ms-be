package com.efms.employee_file_ms_be.model;

/**
 * @author Josue Veliz
 */
public class Constants {

    public static class EmployeeTable {
        public static final String NAME = "employee";
    }

    public static class EmployeeHistoryTable {
        public static final String NAME = "employee_history";
    }

    public static class BaseSalaryTable {
        public static final String NAME = "base_salary";
    }

    public static class Payment {
        public static final String NAME = "payment";
    }

    public static class SalaryEvent {
        public static final String NAME = "salary_event";
    }

    public static class Department {
        public static final String NAME = "department";
    }

    public static class Position {
        public static final String NAME = "position";
    }

    public static class Company {
        public static final String NAME = "company";
    }

    public static class Branch {
        public static final String NAME = "branch";
    }

    public static class Memorandum {
        public static final String NAME = "memorandum";
    }

    public static class File {
        public static final String NAME = "file";
    }

    public static class Absence {
        public static final String NAME = "absence";
    }

    public static class Advance {
        public static final String NAME = "advance";
    }

    public static class GeneralSettings {
        public static final String NAME = "general_settings";
    }

    public static class Vacation {
        public static final String NAME = "vacation";
    }

    public static class TypeSpecifications {
        public static final int NUMERIC_PRECISION = 12;

        public static final int NUMERIC_SCALE = 2;
    }

    public static class MemorandumType {
        public static final String VERBAL_WARNING = "Amonestación Verbal";
        public static final String WRITTEN_WARNING = "Amonestación Escrita";
        public static final String SUSPENSION = "Suspensión";
        public static final String CALL_TO_ATTENTION = "Llamada de Atención";
        public static final String RECOGNITION = "Reconocimiento";
        public static final String CONGRATULATION = "Felicitación";
        public static final String PERFORMANCE_BONUS = "Bono por Desempeño";
        public static final String OTHER = "Otro";
    }
}
