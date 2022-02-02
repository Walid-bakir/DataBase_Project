import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.*;

class Home extends JFrame{
  JButton b1, b2;
  Home(Connection connection){
    Font f=new Font("Arial",Font.BOLD,24);
    JLabel l1=new JLabel("Welcome");
    l1.setFont(f);
    l1.setBounds(70,40,200,40);
    add(l1);
    b1=new JButton("Login");
    b1.setBounds(170,150,100,30);
    add(b1);
    b2 = new JButton("Signin");
    b2.setBounds(170,200, 100, 30);
    add(b2);
    b1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
         setVisible(false);
         new Login(connection);
      }
    });
    b2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
         setVisible(false);
         new Enregistrement(connection);
      }
    });
    setLayout(null);
    setVisible(true);
    setSize(400,400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }
}
