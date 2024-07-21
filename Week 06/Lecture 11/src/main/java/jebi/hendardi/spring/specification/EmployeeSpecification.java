package jebi.hendardi.spring.specification;

import jebi.hendardi.spring.dto.SearchCriteria;
import jebi.hendardi.spring.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

public class EmployeeSpecification implements Specification<Employee> {

    private final SearchCriteria criteria;

    public EmployeeSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
