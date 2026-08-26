package dev.rodrigosambade.empleados;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeRepositoryTest {

    @Test
    void performsCrudLifecycle() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:mem:testemple;DB_CLOSE_DELAY=-1",
                "sa",
                "")) {
            EmployeeRepository repository = new EmployeeRepository();
            repository.init(connection);

            Employee employee = repository.add(
                    connection,
                    "Ana",
                    "Dev",
                    new BigDecimal("2500"));

            assertEquals(1, repository.all(connection).size());
            assertTrue(repository.updateSalary(
                    connection,
                    employee.id(),
                    new BigDecimal("2600")));
            assertTrue(repository.delete(connection, employee.id()));
            assertTrue(repository.all(connection).isEmpty());
        }
    }
}
