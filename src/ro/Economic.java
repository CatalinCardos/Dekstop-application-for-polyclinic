package ro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;
import java.util.Vector;

public class Economic {
        private final static String USERNAME = "root";
        private final static String PASSWORD = "123456789";
        private final static String DB_NAME = "policlinica";
        private final static String CONNECTION_LINK = "jdbc:mysql://localhost:3306/";
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JButton butonSalarii;
    private JButton butonDatePersonale;
    private JLabel nameLabel;
    private JPanel root;
    private JPanel salariiAngajatiPanel;
    private JPanel panelDatePersonale;
    private JButton butonDelogare;
    private JButton profitPolicliniciButton;
    private JPanel profitPoliclinicaPanel;
    private JButton profitClinicaButton;
    private JButton profitMedicButton;
    private JPanel profitClinicaPanel;
    private JPanel profitMediciPanel;
    private JTextField textFieldProfitClinica;
    private JButton salariiCautareButton;
    private JButton vizualizareSalariiButton;
    private JPanel modificareSalariiOKPanel;
    private JPanel modificareSalariiNotOKPanel;
    private JPanel angajatiPanel;
    private JTextField textFieldNume;
    private JTextField textFieldPrenume;
    private JTextField textFieldCNP;
    private JTextField textFieldTelefon;
    private JTextField textFieldTara;
    private JTextField textFieldOras;
    private JTextField textFieldStrada;
    private JTextField textFieldNumar;
    private JTextField textFieldFunctie;
    private JTextField textFieldSalariu;
    private JTextField textFieldNumarOre;
    private JTextField textFieldEmail;
    private JTextField textFieldIBan;
    private JTable table1;
    private JTable table2;
    private JList profitUnitateMedicalaList;
    private JList profitSpecialitateList;
    private JComboBox button1;
    private JComboBox button2;
    private JPanel panelTextFields;
    private JPanel panelLabel;
    private JLabel numeLabel;
    private JLabel prenumeLabel;
    private JLabel CNPLabel;
    private JLabel telefonLabel;
    private JLabel orasLabel;
    private JLabel stradaLabel;
    private JLabel taraLabel;
    private JLabel functieLabel;
    private JLabel numarLabel;
    private JLabel salariuLabel;
    private JLabel numarOreLabel;
    private JButton profitMediciButton;
    private JButton buttonSalariuAngajat;
    private JPanel panelSalar;
    private JComboBox textFieldLunaSalar;
    private JComboBox textFieldAnSalar;
    private JTextField textFieldSalar;
    private JButton afisareSalarbutton;
    private Vector<String> stringListUnitate;
    private PreparedStatement pst;
    private ResultSet resultSet;
    private Connection connection;

    public Economic()
    {
        try {
        connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);


    } catch (SQLException d) {
        d.printStackTrace();
    }
        butonSalarii.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Connection connection;
                PreparedStatement pst;
                PreparedStatement pst1;
                PreparedStatement pst2;

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Nume angajat");
                model.addColumn("Prenume angajat");
                model.addColumn("Salariu");

                table1.setModel(model);
                table1.setRowHeight(30);
                table1.setFillsViewportHeight(true);
                table1.setVisible(true);
                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);

                    Statement statement = connection.createStatement();
                    pst = connection.prepareStatement("select id, numar_ore, salariu_negociat, data_angajare from angajat");
                    ResultSet resultSet = pst.executeQuery();
                    while (resultSet.next()) {
                        int id_angajat = resultSet.getInt("id");
                        int nr_ore = resultSet.getInt("numar_ore");
                        double salar = resultSet.getDouble("salariu_negociat");
                        Date data = resultSet.getDate("data_angajare");
                        Date date = new Date();
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int year = localDate.getYear();
                        int month = localDate.getMonthValue();
                        pst1 = connection.prepareStatement("select date_personale.nume, date_personale.prenume from angajat join utilizator on angajat.id_utilizator = utilizator.id join date_personale on date_personale.id = utilizator.id_date_personale where angajat.id = ?");
                        pst1.setInt(1,id_angajat);
                        ResultSet resultSet1 = pst1.executeQuery();
                        double salariu_angajat;
                        pst2= connection.prepareStatement("select salariu_angajat( ?, ?, ?, ?, ?, ? )");
                        pst2.setInt(1,id_angajat);
                        pst2.setInt(2, month);
                        pst2.setInt(3, nr_ore);
                        pst2.setDouble(4, salar);
                        pst2.setDate(5, (java.sql.Date) data);
                        pst2.setInt(6, year);

                        ResultSet resultSet2 = pst2.executeQuery();

                        Vector<String> angajatOZi = new Vector<>();

                        while( resultSet1.next() )
                        {
                            angajatOZi.add(resultSet1.getString("date_personale.nume"));
                            angajatOZi.add(resultSet1.getString("date_personale.prenume"));

                            while( resultSet2.next() )
                            {
                                salariu_angajat = resultSet2.getDouble(1);
                                angajatOZi.add(String.valueOf(salariu_angajat) + " lei");
                            }

                        }
                        model.addRow(angajatOZi);
                    }
                } catch (SQLException d) {
                    d.printStackTrace();
                }
                root.removeAll();
                root.add(salariiAngajatiPanel);
                root.repaint();
                root.revalidate();
            }
        });
        butonDatePersonale.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                root.removeAll();
                root.add(panelDatePersonale);
                root.repaint();
                root.revalidate();
                Connection connection;
                PreparedStatement pst;
                PreparedStatement pst2;
                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);

                    Statement statement = connection.createStatement();
                    pst = connection.prepareStatement("select * from adresa join date_personale on adresa.id = date_personale.id_adresa where date_personale.id = ?");
                    pst.setInt(1, Login_gui.idDatePersonale);
                    ResultSet resultSet = pst.executeQuery();
                    pst2 = connection.prepareStatement("select * from angajat join utilizator on utilizator.id = angajat.id_utilizator where utilizator.id = ?");
                    pst2.setInt(1, Login_gui.id);
                    ResultSet resultSet2 = pst2.executeQuery();
                    while (resultSet.next()) {

                        textFieldNume.setText(resultSet.getString("nume"));
                        textFieldPrenume.setText(resultSet.getString("prenume"));
                        textFieldCNP.setText(resultSet.getString("CNP"));
                        textFieldTelefon.setText(resultSet.getString("nr_telefon"));
                        textFieldEmail.setText(resultSet.getString("email"));
                        textFieldIBan.setText(resultSet.getString("iban"));
                        textFieldTara.setText(resultSet.getString("tara"));
                        textFieldOras.setText(resultSet.getString("oras"));
                        textFieldStrada.setText(resultSet.getString("strada"));
                        textFieldNumar.setText(resultSet.getString("nr"));
                        while (resultSet2.next())
                        {
                            textFieldFunctie.setText(resultSet2.getString("functie"));
                            textFieldNumarOre.setText(resultSet2.getString("numar_ore"));
                            textFieldSalariu.setText(resultSet2.getString("salariu_negociat"));
                        }

                    }
                } catch (SQLException d) {
                    d.printStackTrace();
                }
            }
        });
        profitPolicliniciButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {


                Connection connection;
                PreparedStatement pst;

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("An");
                model.addColumn("Luna");
                model.addColumn("Profit");

                table2.setModel(model);
                table2.setRowHeight(30);
                table2.setFillsViewportHeight(true);
                table2.setVisible(true);

                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    Date date = new Date();
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int month = localDate.getMonthValue();
                    int year = localDate.getYear();
                    int k = 0;
                    boolean ok = false;
                   for(int i=month;k<12;i--)
                   {
                       k++;
                       if(i==0)
                       {
                           i=12;
                           year--;
                       }
                       pst = connection.prepareStatement("select profit_policlinici(?, ?)");
                       pst.setInt(1, i);
                       pst.setInt(2, year);
                       ResultSet resultSet = pst.executeQuery();

                       Vector<String> unProfit = new Vector<>();

                       String monthName = new String("");
                       switch(i){
                           case 1 : monthName = "Ianuarie";break;
                           case 2 : monthName = "Februarie";break;
                           case 3 : monthName = "Martie";break;
                           case 4 : monthName = "Aprile";break;
                           case 5 : monthName = "Mai";break;
                           case 6 : monthName = "Iunie";break;
                           case 7 : monthName = "Iulie";break;
                           case 8 : monthName = "August";break;
                           case 9 : monthName = "Septembrie";break;
                           case 10 : monthName = "Octombrie";break;
                           case 11 : monthName = "Noiembrie";break;
                           default : monthName = "Decembrie";break;
                       }

                       while(resultSet.next())
                       {
                           unProfit.add(String.valueOf(year));
                           unProfit.add(monthName);
                           unProfit.add(resultSet.getString(1) + " lei");

                       }
                       model.addRow(unProfit);
                   }


                } catch (SQLException d) {
                    d.printStackTrace();
                }
                root.removeAll();
                root.add(profitPoliclinicaPanel);
                root.repaint();
                root.revalidate();

            }
        });
        butonDelogare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.mainPage.setContentPane(new Login_gui().getPanel());
                Main.mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Main.mainPage.pack();
                Main.mainPage.setVisible(true);
            }
        });
        profitMediciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection connection;
                PreparedStatement pst;

                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    pst = connection.prepareStatement("select denumire from unitate_medicala");
                    ResultSet resultSet = pst.executeQuery();

                    while(resultSet.next())
                    {
                        button1.addItem(resultSet.getString(1));
                    }

                } catch (SQLException d) {
                    d.printStackTrace();
                }

                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    pst = connection.prepareStatement("select distinct denumire from specialitate");
                    ResultSet resultSet = pst.executeQuery();

                    while(resultSet.next())
                    {
                        button2.addItem(resultSet.getString(1));
                    }

                } catch (SQLException d) {
                    d.printStackTrace();
                }


                root.removeAll();
                root.add(profitMediciPanel);
                root.repaint();
                root.revalidate();
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection connection;
                PreparedStatement pst;
                stringListUnitate = new Vector<>();

                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    pst = connection.prepareStatement("select date_personale.nume, date_personale.prenume, medic.id, medic.procent_servicii, utilizator.nume_utilizator from utilizator join angajat on utilizator.id=angajat.id_utilizator join date_personale on date_personale.id=utilizator.id_date_personale join medic on medic.id_angajat=angajat.id join unitate_utilizatori on utilizator.id=unitate_utilizatori.id_utilizator join unitate_medicala on unitate_medicala.id=unitate_utilizatori.id_unitate where unitate_medicala.denumire=?;");
                    pst.setString(1, button1.getSelectedItem().toString());
                    ResultSet resultSet = pst.executeQuery();

                    while(resultSet.next())
                    {
                        PreparedStatement pst2;
                        Date date = new Date();
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int month = localDate.getMonthValue();
                        int year = localDate.getYear();
                        pst2 = connection.prepareStatement("select profit_generat_de_medic(?, ?, ?, ?)");
                        pst2.setInt(1, month);
                        pst2.setInt(2, year);
                        pst2.setInt(3, resultSet.getInt(3));
                        pst2.setDouble(4, resultSet.getDouble(4));
                        ResultSet resultSet1 = pst2.executeQuery();
                        while(resultSet1.next())
                        {
                            stringListUnitate.add(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(5) + " " + resultSet1.getString(1) + " lei");
                        }
                    }
                    profitUnitateMedicalaList.setListData(stringListUnitate);
                } catch (SQLException d) {
                    d.printStackTrace();
                }

            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection;
                PreparedStatement pst;
                stringListUnitate = new Vector<>();

                try {
                    connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    pst = connection.prepareStatement("select date_personale.nume, date_personale.prenume, medic.id, medic.procent_servicii, utilizator.nume_utilizator from utilizator join angajat on utilizator.id=angajat.id_utilizator join date_personale on date_personale.id=utilizator.id_date_personale join medic on medic.id_angajat=angajat.id join specialitate on specialitate.id_medic=medic.id where specialitate.denumire=?;");
                    pst.setString(1, button2.getSelectedItem().toString());
                    ResultSet resultSet = pst.executeQuery();

                    while(resultSet.next())
                    {
                        PreparedStatement pst2;
                        Date date = new Date();
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        int month = localDate.getMonthValue();
                        int year = localDate.getYear();
                        pst2 = connection.prepareStatement("select profit_generat_de_medic(?, ?, ?, ?)");
                        pst2.setInt(1, month);
                        pst2.setInt(2, year);
                        pst2.setInt(3, resultSet.getInt(3));
                        pst2.setDouble(4, resultSet.getDouble(4));
                        ResultSet resultSet1 = pst2.executeQuery();
                        while(resultSet1.next())
                        {
                            stringListUnitate.add(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(5) + " " + resultSet1.getString(1) + " lei");
                        }
                    }
                    profitSpecialitateList.setListData(stringListUnitate);
                } catch (SQLException d) {
                    d.printStackTrace();
                }
            }
        });
        afisareSalarbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idAngajat = 0;
                double salariuNeg = 0.0;
                int nrOre = 0;
                double salariu = 0;
                java.util.Date dataAngajare = new java.util.Date();
                java.util.Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int month = localDate.getMonthValue();
                int year = localDate.getYear();
                try {

                    pst = connection.prepareStatement("select angajat.id, angajat.salariu_negociat, angajat.numar_ore,angajat.data_angajare from angajat\n" +
                            "join utilizator on angajat.id_utilizator = utilizator.id\n" +
                            "join date_personale on date_personale.id = utilizator.id_date_personale\n" +
                            "where date_personale.id = ?; ");
                    pst.setInt(1,Login_gui.idDatePersonale);
                    resultSet = pst.executeQuery();
                    while(resultSet.next())
                    {
                        idAngajat = resultSet.getInt("angajat.id");
                        salariuNeg = resultSet.getDouble("angajat.salariu_negociat");
                        nrOre = resultSet.getInt("angajat.numar_ore");
                        dataAngajare = resultSet.getDate("angajat.data_angajare");
                    }
                } catch (SQLException d) {
                    d.printStackTrace();
                }

                if((dataAngajare.getYear()+1900) < Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()) && Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()) < year)
                {
                    try {

                        pst = connection.prepareStatement("select salariu_angajat(?, ?, ?, ?, ?, ?);");
                        pst.setInt(1,idAngajat);
                        pst.setInt(2, Integer.parseInt(textFieldLunaSalar.getSelectedItem().toString()));
                        pst.setInt(3,nrOre);
                        pst.setDouble(4, salariuNeg);
                        pst.setDate(5, (java.sql.Date) dataAngajare);
                        pst.setInt(6, Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()));
                        resultSet = pst.executeQuery();
                        while(resultSet.next())
                        {

                            salariu = resultSet.getDouble(1);
                        }
                    } catch (SQLException d) {
                        d.printStackTrace();
                    }
                    textFieldSalar.setText(String.valueOf(salariu));
                }
                else
                if((dataAngajare.getYear()+1900) == Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()) && (dataAngajare.getMonth() + 1) < Integer.parseInt(textFieldLunaSalar.getSelectedItem().toString()))
                {
                    try {

                        pst = connection.prepareStatement("select salariu_angajat(?, ?, ?, ?, ?, ?);");
                        pst.setInt(1,idAngajat);
                        pst.setInt(2, Integer.parseInt(textFieldLunaSalar.getSelectedItem().toString()));
                        pst.setInt(3,nrOre);
                        pst.setDouble(4, salariuNeg);
                        pst.setDate(5, (java.sql.Date) dataAngajare);
                        pst.setInt(6, Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()));
                        resultSet = pst.executeQuery();
                        while(resultSet.next())
                        {

                            salariu = resultSet.getDouble(1);
                        }
                    } catch (SQLException d) {
                        d.printStackTrace();
                    }
                    textFieldSalar.setText(String.valueOf(salariu));

                }
                else
                if(Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()) == year && month > Integer.parseInt(textFieldLunaSalar.getSelectedItem().toString()))
                {
                    try {

                        pst = connection.prepareStatement("select salariu_angajat(?, ?, ?, ?, ?, ?);");
                        pst.setInt(1,idAngajat);
                        pst.setInt(2, Integer.parseInt(textFieldLunaSalar.getSelectedItem().toString()));
                        pst.setInt(3,nrOre);
                        pst.setDouble(4, salariuNeg);
                        pst.setDate(5, (java.sql.Date) dataAngajare);
                        pst.setInt(6, Integer.parseInt(textFieldAnSalar.getSelectedItem().toString()));
                        resultSet = pst.executeQuery();
                        while(resultSet.next())
                        {

                            salariu = resultSet.getDouble(1);
                        }
                    } catch (SQLException d) {
                        d.printStackTrace();
                    }
                    textFieldSalar.setText(String.valueOf(salariu));
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Nu exista acest salar!");
                    textFieldSalar.setText("");
                }
            }
        });
        buttonSalariuAngajat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                root.removeAll();
                root.add(panelSalar);
                root.repaint();
                root.revalidate();
            }
        });
    }


    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel)
    {
        this.mainPanel = mainPanel;
    }

    public JPanel getMenuPanel()
    {
        return menuPanel;
    }

    public void setMenuPanel(JPanel menuPanel)
    {
        this.menuPanel = menuPanel;
    }

    public JButton getButonOperatiiFinanciare()
    {
        return butonSalarii;
    }

    public void setButonOperatiiFinanciare(JButton butonOperatiiFinanciare)
    {
        this.butonSalarii = butonOperatiiFinanciare;
    }

    public JButton getButonDatePersonale()
    {
        return butonDatePersonale;
    }

    public void setButonDatePersonale(JButton butonDatePersonale)
    {
        this.butonDatePersonale = butonDatePersonale;
    }

    public JLabel getNameLabel()
    {
        return nameLabel;
    }

    public void setNameLabel(JLabel nameLabel)
    {
        this.nameLabel = nameLabel;
    }

    public JPanel getOperatiiPanel()
    {
        return salariiAngajatiPanel;
    }

    public void setOperatiiPanel(JPanel operatiiPanel)
    {
        this.salariiAngajatiPanel = operatiiPanel;
    }

    public JPanel getDatePanel()
    {
        return panelDatePersonale;
    }

    public void setDatePanel(JPanel datePanel)
    {
        this.panelDatePersonale = datePanel;
    }

    public JButton getButonDelogare()
    {
        return butonDelogare;
    }

    public void setButonDelogare(JButton butonDelogare)
    {
        this.butonDelogare = butonDelogare;
    }

    public JPanel getRoot()
    {
        return root;
    }

    public void setRoot(JPanel root)
    {
        this.root = root;
    }
}
