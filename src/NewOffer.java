import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


class NewOffer extends JFrame{
  JButton b1;
  JLabel l2, l1;
  JTextField t1;
  NewOffer(Connection connection, int id_product, String email){
    Font f=new Font("Arial",Font.BOLD,24);
    l1=new JLabel("New Offer");
    l1.setFont(f);
    l1.setBounds(70,40,200,40);
    add(l1);
    l2=new JLabel("Price offer");
    t1=new JTextField();
    l2.setBounds(70,100,100,20);
    t1.setBounds(70,120,200,30);
    add(t1);
    add(l2);
    b1=new JButton("Submit");
    b1.setBounds(170,150,100,30);
    add(b1);
    b1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
         try {
       String cnt = "SELECT Count(*) AS Nbr FROM OFFERS WHERE PRODUCT_ID = '"+id_product+"'";
       String pri = "SELECT CURRENT_PRICE FROM PRODUCT WHERE PRODUCT_ID = '"+id_product+"'";
       PreparedStatement  stm = connection.prepareStatement(cnt);
       PreparedStatement  stm2 = connection.prepareStatement(pri);
       ResultSet price = stm2.executeQuery();
       ResultSet count = stm.executeQuery();
       int c = 0;
       double p = 0;
       while(count.next()){
         c = count.getInt("Nbr");
       }
       while(price.next()){
         p = price.getFloat("CURRENT_PRICE");
       }
       double pp = Double.parseDouble(t1.getText());
       if(p < pp && c<5){
         String i = "SELECT USER_ID FROM USERS WHERE EMAIL_ADDRESS='"+email+"'";
         PreparedStatement  stm4 = connection.prepareStatement(i);
         ResultSet idd = stm4.executeQuery();
         int id_ = 0;
         while(idd.next()){id_ = idd.getInt("USER_ID");}
         String of = "INSERT INTO OFFERS VALUES(sysdate, "+id_product+", "+pp+", "+id_+"  )";
         String update = "UPDATE PRODUCT SET CURRENT_PRICE = "+pp+" WHERE PRODUCT_ID ="+id_product+"";
         PreparedStatement  stm3 = connection.prepareStatement(of);
         stm3.executeQuery();
         PreparedStatement  st = connection.prepareStatement(update);
         st.executeQuery();
         setVisible(false);
         System.out.println("offre enregistre");
         if(c==4){
           String del = "DELETE FROM PRODUCT WHERE PRODUCT_ID = "+id_product+"";
           PreparedStatement  stm5 = connection.prepareStatement(del);
           stm5.executeQuery();
           System.out.println("congratulations");
         }

       }
       }catch (SQLException e) {
         e.printStackTrace ();
       }

      }
    });
    setLayout(null);
    setSize(400,400);


  }
}
