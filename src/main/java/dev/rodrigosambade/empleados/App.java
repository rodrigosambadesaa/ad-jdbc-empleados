package dev.rodrigosambade.empleados;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
public final class App{
    public static void main(String[]a){
        SwingUtilities.invokeLater(App::show);
    }
    static void show(){
        try{
            Connection c=DriverManager.getConnection("jdbc:h2:mem:empleados;DB_CLOSE_DELAY=-1","sa","");
            EmployeeRepository r=new EmployeeRepository();
            r.init(c);
            JFrame f=new JFrame("Empleados");
            DefaultTableModel m=new DefaultTableModel(new Object[]{
                "ID","Nombre","Puesto","Salario"
            }
            ,0){
                public boolean isCellEditable(int r,int c){
                    return false;
                }
            }
            ;
            JTable t=new JTable(m);
            JTextField n=new JTextField(10),j=new JTextField(10),s=new JTextField(8);
            Runnable load=()->{
                try{
                    m.setRowCount(0);
                    for(Employee e:r.all(c))m.addRow(new Object[]{
                        e.id(),e.name(),e.job(),e.salary()
                    }
                    );
                } catch(SQLException e){
                    throw new RuntimeException(e);
                }
            }
            ;
            JButton add=new JButton("Añadir"),del=new JButton("Eliminar");
            add.addActionListener(e->{
                try{
                    r.add(c,n.getText(),j.getText(),new BigDecimal(s.getText()));
                    load.run();
                } catch(Exception x){
                    JOptionPane.showMessageDialog(f,x.getMessage());
                }
            }
            );
            del.addActionListener(e->{
                int row=t.getSelectedRow();
                if(row>=0)try{
                    r.delete(c,(Integer)m.getValueAt(row,0));
                    load.run();
                } catch(SQLException x){
                    JOptionPane.showMessageDialog(f,x.getMessage());
                }
            }
            );
            JPanel p=new JPanel();
            p.add(new JLabel("Nombre"));
            p.add(n);
            p.add(new JLabel("Puesto"));
            p.add(j);
            p.add(new JLabel("Salario"));
            p.add(s);
            p.add(add);
            p.add(del);
            f.add(p,BorderLayout.NORTH);
            f.add(new JScrollPane(t));
            f.setSize(760,420);
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            f.addWindowListener(new java.awt.event.WindowAdapter(){
                @Override public void windowClosed(java.awt.event.WindowEvent e){
                    try{
                        c.close();
                    } catch(SQLException ignored){
                    }
                }
            }
            );
            load.run();
            f.setVisible(true);
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
