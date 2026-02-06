package com.efms.employee_file_ms_be.model.repository.specification;

import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import com.efms.employee_file_ms_be.model.domain.Payment;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentSpecification {

    public static Specification<Payment> withFilters(
            String search,
            String ci,
            String email,
            String phone,
            EmployeeType type,
            EmployeeStatus status,
            Boolean isDisassociated,
            UUID branchId,
            UUID positionId,
            Integer period,
            UUID companyId
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (companyId != null) {
                predicates.add(criteriaBuilder.equal(root.get("companyId"), companyId));
            }

            if (period != null) {
                predicates.add(criteriaBuilder.equal(root.get("period"), period));
            }

            if (search != null && !search.isBlank()) {
                String searchPattern = "%" + search.toLowerCase() + "%";

                Expression<String> firstNameExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("firstName")
                );

                Expression<String> lastNameExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("lastName")
                );

                Expression<String> fullNameExpr = criteriaBuilder.concat(
                        criteriaBuilder.concat(firstNameExpr, " "),
                        lastNameExpr
                );

                Expression<String> contractCompanyExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("contractCompany")
                );

                Expression<String> contractPositionExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("contractPosition")
                );

                Predicate firstNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(firstNameExpr),
                        searchPattern
                );

                Predicate lastNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(lastNameExpr),
                        searchPattern
                );

                Predicate fullNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(fullNameExpr),
                        searchPattern
                );

                Predicate contractCompanyMatch = criteriaBuilder.and(
                        criteriaBuilder.isNotNull(contractCompanyExpr),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(contractCompanyExpr),
                                searchPattern
                        )
                );

                Predicate contractPositionMatch = criteriaBuilder.and(
                        criteriaBuilder.isNotNull(contractPositionExpr),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(contractPositionExpr),
                                searchPattern
                        )
                );

                predicates.add(criteriaBuilder.or(
                        firstNameMatch,
                        lastNameMatch,
                        fullNameMatch,
                        contractCompanyMatch,
                        contractPositionMatch
                ));
            }

            if (ci != null && !ci.isBlank()) {
                Expression<String> ciExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("ci")
                );

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(ciExpr),
                        "%" + ci.toLowerCase() + "%"
                ));
            }

            if (email != null && !email.isBlank()) {
                Expression<String> emailExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("email")
                );

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(emailExpr),
                        "%" + email.toLowerCase() + "%"
                ));
            }

            if (phone != null && !phone.isBlank()) {
                Expression<String> phoneExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("phone")
                );

                predicates.add(criteriaBuilder.like(
                        phoneExpr,
                        "%" + phone + "%"
                ));
            }

            if (type != null) {
                Expression<String> typeExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("type")
                );

                predicates.add(criteriaBuilder.equal(typeExpr, type.name()));
            }

            if (status != null) {
                Expression<String> statusExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("status")
                );

                predicates.add(criteriaBuilder.equal(statusExpr, status.name()));
            }

            if (isDisassociated != null) {
                Expression<String> isDisassociatedExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("isDisassociated")
                );

                predicates.add(criteriaBuilder.equal(isDisassociatedExpr, isDisassociated.toString()));
            }

            if (branchId != null) {
                Expression<String> branchIdExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("branchId")
                );

                predicates.add(criteriaBuilder.equal(branchIdExpr, branchId.toString()));
            }

            if (positionId != null) {
                Expression<String> positionIdExpr = criteriaBuilder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get("employeeDetails"),
                        criteriaBuilder.literal("positionId")
                );

                predicates.add(criteriaBuilder.equal(positionIdExpr, positionId.toString()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}