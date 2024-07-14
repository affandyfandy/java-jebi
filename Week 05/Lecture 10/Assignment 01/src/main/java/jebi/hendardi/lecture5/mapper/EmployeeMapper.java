package jebi.hendardi.lecture5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import jebi.hendardi.lecture5.dto.EmployeeDTO;
import jebi.hendardi.lecture5.model.Employee;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "dob", target = "dob"),
        @Mapping(source = "phone", target = "phone"),
        @Mapping(source = "email", target = "email"),
        @Mapping(source = "address", target = "address"),
        @Mapping(source = "department", target = "department")
    })
    EmployeeDTO toDTO(Employee employee);

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "dob", target = "dob"),
        @Mapping(source = "phone", target = "phone"),
        @Mapping(source = "email", target = "email"),
        @Mapping(source = "address", target = "address"),
        @Mapping(source = "department", target = "department")
    })
    Employee toEntity(EmployeeDTO employeeDTO);

    List<EmployeeDTO> toDTOs(List<Employee> employees);

    List<Employee> toEntities(List<EmployeeDTO> employeeDTOs);
}
