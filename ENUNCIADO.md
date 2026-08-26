# Enunciado original — Empleados

Fuente: `Ejercicio Empleados.pdf` de la carpeta de Acceso a Datos en Google Drive.

## EJERCICIO EMPLEADOS

Entra en phpMyAdmin y crea una base de datos llamada **`empresa`**. A continuación, abre la base de datos e importa el archivo **`empresa.sql`**. Comprueba que se han creado correctamente las tablas de departamentos y empleados.

Se trata de crear una aplicación que permita visualizar en un `JTable` a los empleados. La aplicación deberá permitir realizar:

### Ordenación de los empleados

Se podrá ordenar en base a cualquier campo de los disponibles tanto en orden ascendente como en descendente.

### Filtrado de empleados

Se podrá filtrar a los empleados en base al puesto que ocupan y al departamento en que trabajan.

Al arrancar la aplicación aparecerá la ventana sin datos de empleados. Al pulsar el botón **Mostrar** se mostrarán los empleados de acuerdo a los criterios de ordenación y filtrado seleccionados.

Ejemplos indicados en el enunciado:

- Sin ordenación ni filtrado: `SELECT * FROM empleados`
- Ordenación por salario descendente: `SELECT * FROM empleados ORDER BY salario DESC`
- Ordenación por salario descendente y departamento 30: `SELECT * FROM empleados WHERE num_departamento=30 ORDER BY salario DESC`

## Material asociado

La base de datos original requerida por el ejercicio se incluye en [`database/empresa.sql`](database/empresa.sql).
