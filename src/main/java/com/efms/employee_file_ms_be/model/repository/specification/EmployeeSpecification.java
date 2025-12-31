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
            UUID companyId
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
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch, fullNameMatch));
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

            if (query != null) {
                root.fetch("position", JoinType.LEFT).fetch("department", JoinType.LEFT);
                root.fetch("branch", JoinType.LEFT);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}