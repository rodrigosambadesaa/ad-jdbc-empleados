package dev.rodrigosambade.empleados;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public final class App {

    private static final String DATABASE_URL = "jdbc:h2:mem:empleados;DB_CLOSE_DELAY=-1";

    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::show);
    }

    static void show() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, "sa", "");
            EmployeeRepository repository = new EmployeeRepository();
            repository.init(connection);

            JFrame frame = createFrame(connection, repository);
            frame.setVisible(true);
        } catch (SQLException exception) {
            throw new IllegalStateException("No se pudo abrir la base de datos", exception);
        }
    }

    private static JFrame createFrame(
            Connection connection,
            EmployeeRepository repository) throws SQLException {
        DefaultTableModel tableModel = createTableModel();
        JTable table = new JTable(tableModel);
        JTextField name = new JTextField(10);
        JTextField job = new JTextField(10);
        JTextField salary = new JTextField(8);
        JButton add = new JButton("Añadir");
        JButton delete = new JButton("Eliminar");

        loadEmployees(tableModel, repository, connection);
        add.addActionListener(event -> addEmployee(
                tableModel,
                name,
                job,
                salary,
                repository,
                connection));
        delete.addActionListener(event -> deleteSelectedEmployee(
                table,
                tableModel,
                repository,
                connection));

        JFrame frame = new JFrame("Empleados");
        frame.add(createControls(name, job, salary, add, delete), BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setSize(760, 420);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(closeConnectionWhenWindowCloses(connection));
        return frame;
    }

    private static DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Puesto", "Salario"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static JPanel createControls(
            JTextField name,
            JTextField job,
            JTextField salary,
            JButton add,
            JButton delete) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre"));
        panel.add(name);
        panel.add(new JLabel("Puesto"));
        panel.add(job);
        panel.add(new JLabel("Salario"));
        panel.add(salary);
        panel.add(add);
        panel.add(delete);
        return panel;
    }

    private static void addEmployee(
            DefaultTableModel tableModel,
            JTextField name,
            JTextField job,
            JTextField salary,
            EmployeeRepository repository,
            Connection connection) {
        try {
            repository.add(
                    connection,
                    name.getText(),
                    job.getText(),
                    new BigDecimal(salary.getText().trim()));
            loadEmployees(tableModel, repository, connection);
            name.setText("");
            job.setText("");
            salary.setText("");
        } catch (RuntimeException | SQLException exception) {
            showError(exception);
        }
    }

    private static void deleteSelectedEmployee(
            JTable table,
            DefaultTableModel tableModel,
            EmployeeRepository repository,
            Connection connection) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        try {
            int employeeId = (Integer) tableModel.getValueAt(selectedRow, 0);
            repository.delete(connection, employeeId);
            loadEmployees(tableModel, repository, connection);
        } catch (SQLException exception) {
            showError(exception);
        }
    }

    private static void loadEmployees(
            DefaultTableModel tableModel,
            EmployeeRepository repository,
            Connection connection) throws SQLException {
        tableModel.setRowCount(0);
        for (Employee employee : repository.all(connection)) {
            tableModel.addRow(new Object[]{
                    employee.id(),
                    employee.name(),
                    employee.job(),
                    employee.salary()
            });
        }
    }

    private static WindowAdapter closeConnectionWhenWindowCloses(Connection connection) {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                    // Nothing useful can be recovered while the window is closing.
                }
            }
        };
    }

    private static void showError(Exception exception) {
        JOptionPane.showMessageDialog(
                null,
                exception.getMessage(),
                "Empleados",
                JOptionPane.ERROR_MESSAGE);
    }
}
