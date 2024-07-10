package jebi.hendardi.spring.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import jebi.hendardi.spring.model.Employee;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;
    private final EmployeeService employeeService;

    public PdfService(TemplateEngine templateEngine, EmployeeService employeeService) {
        this.templateEngine = templateEngine;
        this.employeeService = employeeService;
    }

    public byte[] generatePdf(List<Employee> employees) throws Exception {
        Context context = new Context();
        context.setVariable("employees", employees);

        double totalSalary = employees.stream()
                                     .mapToDouble(Employee::getSalary)
                                     .sum();
        context.setVariable("totalSalary", totalSalary);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        context.setVariable("currentDate", formattedDateTime);

        Optional<Employee> highestSalaryEmployee = employeeService.getHighestSalaryEmployee();
        Optional<Employee> lowestSalaryEmployee = employeeService.getLowestSalaryEmployee();
        long recordCount = employeeService.getRecordCount();
        OptionalDouble averageSalary = employeeService.getAverageSalary();

        context.setVariable("highestSalaryEmployee", highestSalaryEmployee.orElse(null));
        context.setVariable("lowestSalaryEmployee", lowestSalaryEmployee.orElse(null));
        context.setVariable("recordCount", recordCount);
        context.setVariable("averageSalary", averageSalary.isPresent() ? averageSalary.getAsDouble() : 0);

        String html = templateEngine.process("pdf-template", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        }
    }
}
