import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Enregistrement extends JFrame{
       JLabel label1, label2, label3, label4, label5, label6;
       JTextField text1, text2, text3, text4, text5, text6;
       JButton button;

       public Enregistrement(Connection connection){
         Font f = new Font("Arial",Font.BOLD,24);
         label1 = new JLabel("Login Page");
         label1.setFont(f);
         label2 = new JLabel("Email");
         label3 = new JLabel("Postal_Address");
         label4 = new JLabel("Password");
         label5 = new JLabel("FirstName");
         label6 = new JLabel("LastName");
         button = new JButton("Login");
         text1 = new JTextField();
         text2 = new JTextField();
         text3 = new JTextField();
         text4 = new JTextField();
         text5 = new JTextField();
         text6 = new JTextField();
         label1.setBounds(90,40,200,40);
         label2.setBounds(90,100,100,50);
         label3.setBounds(90,190,100,50);
         label4.setBounds(90,280,100,50);
         label5.setBounds(90,370,100,50);
         label6.setBounds(90,460,100,50);
         text2.setBounds(90,140,200,30);
         text3.setBounds(90,230,200,30);
         text4.setBounds(90,320,200,30);
         text5.setBounds(90,410,200,30);
         text6.setBounds(90,500,200,30);
         button.setBounds(500,600,100,50);
         add(label1);
         add(label2);
         add(label3);
         add(label4);
         add(label5);
         add(label6);
         add(text2);
         add(text3);
         add(text4);
         add(text5);
         add(text6);
         add(button);
         setLayout(null);
   			 setVisible(true);
   			 setSize(800,800);
         button.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent action){
             String email = text2.getText();
             String address = text3.getText();
             String password = text4.getText();
             String prenom = text5.getText();
             String nom = text6.getText();
              try {
              Statement stmt = connection.createStatement();
              String nbr = "SELECT COUNT(*) AS Cnt FROM ID";
              ResultSet res = stmt.executeQuery(nbr);
              int nb = 0;
              while(res.next()){nb = res.getInt("Cnt")+1;}
              String sql = "INSERT INTO ID VALUES ('"+nb+"')";
              PreparedStatement ps2 = connection.prepareStatement(sql);
              ps2.executeQuery();
              String sql2 = "INSERT INTO USERS VALUES ('"+email+"', '"+address+"', '"+password+"','"+prenom+"','"+nom+"','"+nb+"')";
              PreparedStatement ps = connection.prepareStatement(sql2);
              ps.executeQuery();
              setVisible(false);
              new Home(connection);
              } catch (SQLException e) {
                System.err.println("failed");
                e.printStackTrace(System.err);
              }

   				}
         });
}
}
