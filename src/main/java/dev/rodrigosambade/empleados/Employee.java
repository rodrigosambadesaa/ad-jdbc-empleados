package dev.rodrigosambade.empleados;

import java.math.BigDecimal;

public record Employee(
        int id,
        String name,
        String job,
        BigDecimal salary) {
}
