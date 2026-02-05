package com.efms.employee_file_ms_be.model.repository.specification;

import com.efms.employee_file_ms_be.model.domain.Employee;
import com.efms.employee_file_ms_be.model.domain.EmployeeStatus;
import com.efms.employee_file_ms_be.model.domain.EmployeeType;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Josue Veliz
 */
public class EmployeeSpecification {

    public static Specification<Employee> withFilters(
            String search,
            String ci,
            String email,
            String phone,
            EmployeeType type,
            EmployeeStatus status,
            Boolean isDisassociated,
            UUID branchId,
            UUID positionId,
            UUID companyId,
            String contractCompany
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (companyId != null) {
                predicates.add(criteriaBuilder.equal(root.get("companyId"), companyId));
            }

            if (search != null && !search.isBlank()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate firstNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        searchPattern
                );
                Predicate lastNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        searchPattern
                );
                Predicate fullNameMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(
                                criteriaBuilder.concat(
                                        criteriaBuilder.concat(root.get("firstName"), " "),
                                        root.get("lastName")
                                )
                        ),
                        searchPattern
                );

                Predicate contractCompanyMatch = criteriaBuilder.and(
                        criteriaBuilder.isNotNull(root.get("contractCompany")),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("contractCompany")),
                                searchPattern
                        )
                );

                predicates.add(criteriaBuilder.or(
                        firstNameMatch,
                        lastNameMatch,
                        fullNameMatch,
                        contractCompanyMatch
                ));
            }

            if (contractCompany != null && !contractCompany.isBlank()) {
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.isNotNull(root.get("contractCompany")),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("contractCompany")),
                                "%" + contractCompany.toLowerCase() + "%"
                        )
                ));
            }

            if (ci != null && !ci.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("ci")),
                        "%" + ci.toLowerCase() + "%"
                ));
            }

            if (email != null && !email.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                ));
            }

            if (phone != null && !phone.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        root.get("phone"),
                        "%" + phone + "%"
                ));
            }

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (isDisassociated != null) {
                predicates.add(criteriaBuilder.equal(root.get("isDisassociated"), isDisassociated));
            }

            if (branchId != null) {
                predicates.add(criteriaBuilder.equal(root.get("branch").get("id"), branchId));
            }

            if (positionId != null) {
                predicates.add(criteriaBuilder.equal(root.get("position").get("id"), positionId));
            }

            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("position", JoinType.LEFT).fetch("department", JoinType.LEFT);
                root.fetch("branch", JoinType.LEFT);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}