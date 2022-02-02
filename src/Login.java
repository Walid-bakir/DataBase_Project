import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.*;

class Login extends JFrame{
		JLabel l1,l2,l3;
		JTextField t1;
		JPasswordField t2;
		JButton b1;
		HomeUser user;
		Login(Connection connection){
			Font f=new Font("Arial",Font.BOLD,24);
			l1=new JLabel("Login Page");
			l1.setFont(f);
			l2=new JLabel("Email");
			t1=new JTextField();
			l3=new JLabel("Password");
			t2=new JPasswordField();
			b1=new JButton("Login");
			l1.setBounds(70,40,200,40);
			l2.setBounds(70,100,100,20);
			t1.setBounds(70,120,200,30);
			l3.setBounds(70,170,100,20);
			t2.setBounds(70,190,200,30);
			b1.setBounds(170,240,100,30);
			add(l1);
			add(l2);
			add(t1);
			add(l3);
			add(t2);
			add(b1);
			setLayout(null);
			setVisible(true);
			setSize(400,400);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			b1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
            try {
          // Statement statement = connection.createStatement();
          String psw = "SELECT PASSWRD FROM USERS WHERE EMAIL_ADDRESS='"+t1.getText()+"'";
          PreparedStatement ps = connection.prepareStatement(psw);
          ResultSet res = ps.executeQuery();
          while (res.next()){
            if(t2.getText().equals(res.getString("PASSWRD"))){
              setVisible(false);
              user = new HomeUser(connection, t1.getText());
							user.setVisible(true);
            }
          }
        }catch (SQLException e) {
            e.printStackTrace ();
          }
				}
			});
		}
}
