import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

class HomeUser extends JFrame{
  JLabel l1;
  JButton b1, b2, b3, b4;
  NewOffer offer;
  int id = 0;
  HomeUser(Connection connection, String email){
    String name = "";
    try {
    Statement statement = connection.createStatement();
    String psw = "SELECT NAME, USER_ID FROM USERS WHERE EMAIL_ADDRESS='"+email+"'";
    PreparedStatement ps = connection.prepareStatement(psw);
    ResultSet res = ps.executeQuery();

    while (res.next()){
       name = res.getString("NAME");
       id = res.getInt("USER_ID");
    }
    }catch (SQLException e) {
    e.printStackTrace ();
    }

    Font f=new Font("Arial",Font.BOLD,24);
    l1=new JLabel("Welcome "+ name);
    l1.setFont(f);
    l1.setBounds(70,40,250,40);
    add(l1);
    b3=new JButton("Recomended");
    b3.setBounds(50,100,140,30);
    add(b3);
    b2=new JButton("Categories");
    b2.setBounds(200,100,140,30);
    add(b2);
    b1=new JButton("Logout");
    b1.setBounds(200,240,100,30);
    add(b1);
    b4=new JButton("Delete account");
    b4.setBounds(50,240,150,30);
    add(b4);

    b4.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        try{
         String query = "DELETE FROM USERS WHERE EMAIL_ADDRESS='"+email+"'";
         PreparedStatement ps = connection.prepareStatement(query);
         ps.executeQuery();
         }catch (SQLException e) {
         e.printStackTrace();}
         setVisible(false);
         new Home(connection);
      }
    });
    b1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
         setVisible(false);
         new Home(connection);
      }
    });

    b2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
      try
      {

        String query = "SELECT * FROM CATEGORY";
        String cnt = "SELECT COUNT (*) AS Nbr FROM CATEGORY";
        Statement stm2 = connection.createStatement();
        Statement stm = connection.createStatement();
        ResultSet res = stm.executeQuery(query);
        ResultSet count = stm2.executeQuery(cnt);
        int c = 0;
        while(count.next()){
          c = count.getInt("Nbr");
        }
        String columns[] = {"CATEGORIES"};
        String data[][] = new String[c][1];

        int i = 0;
        while (res.next()) {
          String ct = res.getString("CATEGORY_NAME");
          data[i][0] = ct;
          i++;
        }

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
              try{
                String cat_name = data[table.getSelectedRow()][0];
                String sous_cat = "SELECT CATEGORY_NOM FROM SOUS_CATEGORY WHERE CATEGORY_NAME='"+cat_name+"'";
                String cal = "SELECT COUNT (*) AS Nbr FROM SOUS_CATEGORY WHERE CATEGORY_NAME='"+cat_name+"'";
                PreparedStatement ps1 = connection.prepareStatement(cal);
                ResultSet calcul = ps1.executeQuery();
                PreparedStatement ps2 = connection.prepareStatement(sous_cat);
                ResultSet ss_cat = ps2.executeQuery();
                int calc = 0;
                while(calcul.next()){
                  calc = calcul.getInt("Nbr");
                }
                String columns2[] = {"SUB CATEGORIES"};
                String data2[][] = new String[calc][1];
                int j = 0;
                while (ss_cat.next()) {
                  String cc = ss_cat.getString("CATEGORY_NOM");
                  data2[j][0] = cc;
                  j++;
                }
                DefaultTableModel model2 = new DefaultTableModel(data2, columns2);
                JTable table2 = new JTable(model2);
                table2.setShowGrid(true);
                table2.setShowVerticalLines(true);
                table2.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                      try{
                      String cn = data2[table2.getSelectedRow()][0];
                      String req = "SELECT INTITULE,CURRENT_PRICE,DESCRIPTION, URL_PHOTO, PRODUCT.PRODUCT_ID, COUNT(OFFERS.PRODUCT_ID) AS Nb_offres"
                      + " FROM PRODUCT LEFT JOIN OFFERS ON PRODUCT.PRODUCT_ID=OFFERS.PRODUCT_ID"
                      + " WHERE CATEGORY_NOM='"+cn+"' "
                      +" GROUP BY INTITULE, CURRENT_PRICE,  PRODUCT.PRODUCT_ID,DESCRIPTION, URL_PHOTO"
                      + " ORDER BY Nb_offres DESC, UPPER(INTITULE)";
                      String cntt = "SELECT COUNT(*) AS NBR FROM PRODUCT WHERE CATEGORY_NOM='"+cn+"'";
                      PreparedStatement ps3 = connection.prepareStatement(req);
                      PreparedStatement s = connection.prepareStatement(cntt);
                      ResultSet r1 = s.executeQuery();
                      ResultSet r2 = ps3.executeQuery();
                      int cnttt = 0;
                      while(r1.next()){
                        cnttt = r1.getInt("NBR");
                      }

                     String columns3[] = { "Produit", "Price", "Description", "Photo", "nobre d'offres" };
                     String data3[][] = new String[cnttt][5];

                     int i = 0;
                     int[] products_id = new int[cnttt];
                     while (r2.next()) {
                       String pro = r2.getString("INTITULE");
                       int pri = r2.getInt("CURRENT_PRICE");
                       String des = r2.getString("DESCRIPTION");
                       String pho = r2.getString("URL_PHOTO");
                       int id = r2.getInt("PRODUCT_ID");
                       int t = r2.getInt("Nb_offres");
                       data3[i][0] = pro;
                       data3[i][1] = pri+"";
                       data3[i][2] = des;
                       data3[i][3] = pho;
                       data3[i][4] = t+"";
                       products_id[i] = id;
                       i++;
                     }

                     DefaultTableModel model3 = new DefaultTableModel(data3, columns3);
                     JTable table3 = new JTable(model3);
                     table3.setShowGrid(true);
                     table3.setShowVerticalLines(true);
                     table3.addMouseListener(new MouseListener() {
                         @Override
                         public void mouseReleased(MouseEvent e) {
                         }
                         @Override
                         public void mousePressed(MouseEvent e) {
                             int id_product = products_id[table3.getSelectedRow()];
                             offer = new NewOffer(connection, id_product, email);
                             offer.setVisible(true);
                             try{
                               String cara = "SELECT CARAC, VALUE FROM CARACTERISTICS WHERE PRODUCT_ID="+id_product+"";
                               String cccc = "SELECT COUNT(*) AS NBR FROM CARACTERISTICS WHERE PRODUCT_ID="+id_product+" ";
                               PreparedStatement ps10 = connection.prepareStatement(cccc);
                               PreparedStatement ps20 = connection.prepareStatement(cara);
                               ResultSet r20 = ps20.executeQuery();
                               ResultSet r10 = ps10.executeQuery();
                               String columns4[] = { "Caracteristic", "Value"};
                               int ccccc = 0;
                               while(r10.next()){
                                 ccccc = r10.getInt("NBR");
                               }
                               String data4[][] = new String[ccccc][2];
                               int i = 0;
                               while (r20.next()) {
                                 String cr = r20.getString("CARAC");
                                 String vl = r20.getString("VALUE");
                                 data4[i][0] = cr;
                                 data4[i][1] = vl;
                                 i++;
                               }
                               DefaultTableModel model4 = new DefaultTableModel(data4, columns4);
                               JTable table4 = new JTable(model4);
                               table4.setShowGrid(true);
                               table4.setShowVerticalLines(true);
                               JScrollPane pane4 = new JScrollPane(table4);
                               JFrame f4 = new JFrame("Caracteristics");
                               JPanel panel4 = new JPanel();
                               panel4.add(pane4);
                               f4.add(panel4);

                               f4.setSize(500, 250);
                               f4.setVisible(true);
                             }catch(SQLException aee) {
                               aee.printStackTrace();
                             }


                         }
                         @Override
                         public void mouseExited(MouseEvent e) {
                         }
                         @Override
                         public void mouseEntered(MouseEvent e) {
                         }
                         @Override
                         public void mouseClicked(MouseEvent e) {
                         }
                     });

                     JScrollPane pane3 = new JScrollPane(table3);
                     JFrame f3 = new JFrame("Products");
                     JPanel panel3 = new JPanel();
                     panel3.add(pane3);
                     f3.add(panel3);

                     f3.setSize(500, 250);
                     f3.setVisible(true);
                   }catch(SQLException eee) {
                     eee.printStackTrace();
                   }

            }
              @Override
              public void mouseExited(MouseEvent e) {
              }
              @Override
              public void mouseEntered(MouseEvent e) {
              }
              @Override
              public void mouseClicked(MouseEvent e) {
              }

            });
                JScrollPane pane2 = new JScrollPane(table2);
                JFrame f2 = new JFrame("sub ategories");
                JPanel panel2 = new JPanel();
                panel2.add(pane2);
                f2.add(panel2);

                f2.setSize(500, 250);
                f2.setVisible(true);
              }catch(SQLException ee) {
                ee.printStackTrace();
              }
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        ////////////////
        JScrollPane pane = new JScrollPane(table);
        JFrame f = new JFrame("Products");
        JPanel panel = new JPanel();
        panel.add(pane);
        f.add(panel);

        f.setSize(500, 250);
        f.setVisible(true);

      } catch(SQLException e) {
        e.printStackTrace();
      }

      }
    });

b3.addActionListener(new ActionListener(){
  public void actionPerformed(ActionEvent ae){
  try
  {
    String query = "SELECT CATEGORY_NAME, COUNT(O.PRODUCT_ID) AS CNT"
    +" FROM SOUS_CATEGORY S"
    +" JOIN ID_PRODUCT P ON S.CATEGORY_NOM=P.CATEGORY_NOM "
    +" JOIN OFFERS O ON O.PRODUCT_ID = P.PRODUCT_ID"
    +" WHERE O.USER_ID NOT IN("
    +"  SELECT USER_ID "
    +"  FROM OFFERS"
    +"  WHERE DATE_TIME IN"
    +"   ("
    +"  SELECT MAX(DATE_TIME)"
    +"   FROM OFFERS"
    +"   WHERE OFFERS.PRODUCT_ID = P.PRODUCT_ID"
    +"   GROUP BY PRODUCT_ID"
    +"   HAVING  COUNT(PRODUCT_ID)=5"
    +"   )"
    +" )"
    +" and O.USER_ID = '"+id+"'"
    +" GROUP BY CATEGORY_NAME"
    +" ORDER BY CNT DESC";

    String q = "SELECT CATEGORY_NAME FROM("
    +" SELECT CATEGORY_NAME, COUNT(O.PRODUCT_ID)/COUNT(P.PRODUCT_ID) AS CNT"
    +" FROM SOUS_CATEGORY S"
    +" JOIN ID_PRODUCT P ON S.CATEGORY_NOM=P.CATEGORY_NOM"
    +" LEFT JOIN OFFERS O ON O.PRODUCT_ID = P.PRODUCT_ID"
    +" GROUP BY CATEGORY_NAME"
    +" ORDER BY CNT DESC"
    +" )"
    +" WHERE CATEGORY_NAME NOT IN (SELECT CATEGORY_NAME FROM("
    +" SELECT CATEGORY_NAME, COUNT(O.PRODUCT_ID) AS CNT"
    +" FROM SOUS_CATEGORY S"
    +" JOIN ID_PRODUCT P ON S.CATEGORY_NOM=P.CATEGORY_NOM"
    +" JOIN OFFERS O ON O.PRODUCT_ID = P.PRODUCT_ID"
    +" WHERE O.USER_ID NOT IN("
    +"  SELECT USER_ID"
    +"  FROM OFFERS"
    +"  WHERE DATE_TIME IN"
    +"   ("
    +"   SELECT MAX(DATE_TIME)"
    +"   FROM OFFERS"
    +"   WHERE OFFERS.PRODUCT_ID = P.PRODUCT_ID"
    +"   GROUP BY PRODUCT_ID"
    +"   HAVING  COUNT(PRODUCT_ID)=5"
    +"   )"
    +" )"
    +" and O.USER_ID = 10"
    +" GROUP BY CATEGORY_NAME"
    +" ORDER BY CNT DESC"
    +" ))";

    PreparedStatement ppss = connection.prepareStatement(q);
    ResultSet rr = ppss.executeQuery();

    String cnt = "SELECT COUNT (*) AS Nbr FROM CATEGORY";
    Statement stm2 = connection.createStatement();
    Statement stm = connection.createStatement();
    ResultSet res = stm.executeQuery(query);
    ResultSet count = stm2.executeQuery(cnt);
    int c = 0;
    while(count.next()){
      c = count.getInt("Nbr");
    }
    String columns[] = {"CATEGORIES"};
    String data[][] = new String[c][1];

    int i = 0;
    while (res.next()) {
      String ct = res.getString("CATEGORY_NAME");
      data[i][0] = ct;
      // cat[i] = ct;
      i++;
    }
    while(rr.next()){
      String ctt = rr.getString("CATEGORY_NAME");
      data[i][0] = ctt;
      i++;
    }

    DefaultTableModel model = new DefaultTableModel(data, columns);
    JTable table = new JTable(model);
    table.setShowGrid(true);
    table.setShowVerticalLines(true);
    /////////////////
    table.addMouseListener(new MouseListener() {
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
          try{
            String cat_name = data[table.getSelectedRow()][0];
            String sous_cat = "SELECT CATEGORY_NOM FROM SOUS_CATEGORY WHERE CATEGORY_NAME='"+cat_name+"'";
            String cal = "SELECT COUNT (*) AS Nbr FROM SOUS_CATEGORY WHERE CATEGORY_NAME='"+cat_name+"'";
            PreparedStatement ps1 = connection.prepareStatement(cal);
            ResultSet calcul = ps1.executeQuery();
            PreparedStatement ps2 = connection.prepareStatement(sous_cat);
            ResultSet ss_cat = ps2.executeQuery();
            int calc = 0;
            while(calcul.next()){
              calc = calcul.getInt("Nbr");
            }
            String columns2[] = {"SUB CATEGORIES"};
            String data2[][] = new String[calc][1];
            int j = 0;
            while (ss_cat.next()) {
              String cc = ss_cat.getString("CATEGORY_NOM");
              data2[j][0] = cc;
              j++;
            }
            DefaultTableModel model2 = new DefaultTableModel(data2, columns2);
            JTable table2 = new JTable(model2);
            table2.setShowGrid(true);
            table2.setShowVerticalLines(true);
            table2.addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent e) {
                }
                @Override
                public void mousePressed(MouseEvent e) {
                  try{
                  String cn = data2[table2.getSelectedRow()][0];
                  String req = "SELECT INTITULE,CURRENT_PRICE,DESCRIPTION, URL_PHOTO, PRODUCT.PRODUCT_ID, COUNT(OFFERS.PRODUCT_ID) AS Nb_offres"
                  + " FROM PRODUCT LEFT JOIN OFFERS ON PRODUCT.PRODUCT_ID=OFFERS.PRODUCT_ID"
                  + " WHERE CATEGORY_NOM='"+cn+"' "
                  +" GROUP BY INTITULE, CURRENT_PRICE,  PRODUCT.PRODUCT_ID,DESCRIPTION, URL_PHOTO"
                  + " ORDER BY Nb_offres DESC, UPPER(INTITULE)";
                  String cntt = "SELECT COUNT(*) AS NBR FROM PRODUCT WHERE CATEGORY_NOM='"+cn+"'";
                  PreparedStatement ps3 = connection.prepareStatement(req);
                  PreparedStatement s = connection.prepareStatement(cntt);
                  ResultSet r1 = s.executeQuery();
                  ResultSet r2 = ps3.executeQuery();
                  int cnttt = 0;
                  while(r1.next()){
                    cnttt = r1.getInt("NBR");
                  }

                 String columns3[] = { "Produit", "Price", "Description", "Photo", "nobre d'offres" };
                 String data3[][] = new String[cnttt][5];

                 int i = 0;
                 int[] products_id = new int[cnttt];
                 while (r2.next()) {
                   String pro = r2.getString("INTITULE");
                   // String cat = r2.getString("CATEGORY_NOM");
                   int pri = r2.getInt("CURRENT_PRICE");
                   String des = r2.getString("DESCRIPTION");
                   String pho = r2.getString("URL_PHOTO");
                   int id = r2.getInt("PRODUCT_ID");
                   int t = r2.getInt("Nb_offres");
                   data3[i][0] = pro;
                   // data3[i][1] = cat;
                   data3[i][1] = pri+"";
                   data3[i][2] = des;
                   data3[i][3] = pho;
                   data3[i][4] = t+"";
                   products_id[i] = id;
                   i++;
                 }

                 DefaultTableModel model3 = new DefaultTableModel(data3, columns3);
                 JTable table3 = new JTable(model3);
                 table3.setShowGrid(true);
                 table3.setShowVerticalLines(true);
                 /////////////////
                 table3.addMouseListener(new MouseListener() {
                     @Override
                     public void mouseReleased(MouseEvent e) {
                     }
                     @Override
                     public void mousePressed(MouseEvent e) {
                         int id_product = products_id[table3.getSelectedRow()];
                         offer = new NewOffer(connection, id_product, email);
                         offer.setVisible(true);
                         try{
                           String cara = "SELECT CARAC, VALUE FROM CARACTERISTICS WHERE PRODUCT_ID="+id_product+"";
                           String cccc = "SELECT COUNT(*) AS NBR FROM CARACTERISTICS WHERE PRODUCT_ID="+id_product+" ";
                           PreparedStatement ps10 = connection.prepareStatement(cccc);
                           PreparedStatement ps20 = connection.prepareStatement(cara);
                           ResultSet r20 = ps20.executeQuery();
                           ResultSet r10 = ps10.executeQuery();
                           String columns4[] = { "Caracteristic", "Value"};
                           int ccccc = 0;
                           while(r10.next()){
                             ccccc = r10.getInt("NBR");
                           }
                           String data4[][] = new String[ccccc][2];
                           int i = 0;
                           while (r20.next()) {
                             String cr = r20.getString("CARAC");
                             String vl = r20.getString("VALUE");
                             data4[i][0] = cr;
                             data4[i][1] = vl;
                             i++;
                           }
                           DefaultTableModel model4 = new DefaultTableModel(data4, columns4);
                           JTable table4 = new JTable(model4);
                           table4.setShowGrid(true);
                           table4.setShowVerticalLines(true);
                           JScrollPane pane4 = new JScrollPane(table4);
                           JFrame f4 = new JFrame("Caracteristics");
                           JPanel panel4 = new JPanel();
                           panel4.add(pane4);
                           f4.add(panel4);

                           f4.setSize(500, 250);
                           f4.setVisible(true);
                         }catch(SQLException aee) {
                           aee.printStackTrace();
                         }
                     }
                     @Override
                     public void mouseExited(MouseEvent e) {
                     }
                     @Override
                     public void mouseEntered(MouseEvent e) {
                     }
                     @Override
                     public void mouseClicked(MouseEvent e) {
                     }
                 });

                 JScrollPane pane3 = new JScrollPane(table3);
                 JFrame f3 = new JFrame("Products");
                 JPanel panel3 = new JPanel();
                 panel3.add(pane3);
                 f3.add(panel3);

                 f3.setSize(500, 250);
                 f3.setVisible(true);
               }catch(SQLException eee) {
                 eee.printStackTrace();
               }
        }
          @Override
          public void mouseExited(MouseEvent e) {
          }
          @Override
          public void mouseEntered(MouseEvent e) {
          }
          @Override
          public void mouseClicked(MouseEvent e) {
          }

        });
            JScrollPane pane2 = new JScrollPane(table2);
            JFrame f2 = new JFrame("sub ategories");
            JPanel panel2 = new JPanel();
            panel2.add(pane2);
            f2.add(panel2);

            f2.setSize(500, 250);
            f2.setVisible(true);
          }catch(SQLException ee) {
            ee.printStackTrace();
          }
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseClicked(MouseEvent e) {
        }
    });
    JScrollPane pane = new JScrollPane(table);
    JFrame f = new JFrame("Products");
    JPanel panel = new JPanel();
    panel.add(pane);
    f.add(panel);

    f.setSize(500, 250);
    f.setVisible(true);

  } catch(SQLException e) {
    e.printStackTrace();
  }

  }
});

    setLayout(null);
    setSize(400,400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }
}
