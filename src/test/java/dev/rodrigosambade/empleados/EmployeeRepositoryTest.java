package dev.rodrigosambade.empleados;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.sql.DriverManager;
import static org.junit.jupiter.api.Assertions.*;
class EmployeeRepositoryTest{
    @Test void crud()throws Exception{
        try(var c=DriverManager.getConnection("jdbc:h2:mem:testemple;DB_CLOSE_DELAY=-1","sa","")){
            var r=new EmployeeRepository();
            r.init(c);
            var e=r.add(c,"Ana","Dev",new BigDecimal("2500"));
            assertEquals(1,r.all(c).size());
            assertTrue(r.updateSalary(c,e.id(),new BigDecimal("2600")));
            assertTrue(r.delete(c,e.id()));
            assertTrue(r.all(c).isEmpty());
        }
    }
}
