package ro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;


public class Asistent {
    private final static String USERNAME = "root";
    private final static String PASSWORD = "123456789";
    private final static String DB_NAME = "policlinica";
    private final static String CONNECTION_LINK = "jdbc:mysql://localhost:3306/";
    private JPanel mainPanel;
    private JButton buttonDatePersonale;
    private JButton buttonRaportAnalize;
    private JPanel panelMeniu;
    private JPanel rootPanel;
    private JPanel panelRaportAnalize;
    private JPanel panelNumeRaport;
    private JPanel panelDatePersonale;
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
    private JPanel panelTextFields;
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
    private JPanel panelLabel;
    private JTextField textFieldRaportCNP;
    private JButton buttonDeconectare;
    private JLabel labelRaportNume;
    private JButton buttonValidare;
    private JButton buttonConfirmare;
    private JTextField textFieldEmail;
    private JTextField textFieldIBan;
    private JLabel iBanLabel;
    private JLabel emailLKabel;
    private JComboBox comboBox1;
    private JButton buttonCreareRaport;
    private JPanel panelCreareRaport;
    private JTextField textFieldNumeAnaliza;
    private JButton adaugareButton;
    private JTextField textFieldPrenumeAnaliza;
    private JComboBox comboBoxRaportAnaliza;
    private JTextField textFieldRezultatNumeric;
    private JLabel labelPentruRezultatNumeric;
    private JScrollPane scrollPanePentruRaportAnaliza;
    private JList listPentruRaportAnaliza;
    private JLabel labelPentruTextIntrod;
    private JButton buttonSalariuAngajat;
    private JPanel panelSalar;
    private JComboBox textFieldLunaSalar;
    private JComboBox textFieldAnSalar;
    private JTextField textFieldSalar;
    private JButton afisareSalarbutton;
    private ResultSet resultSet;
    private ResultSet resultSet2;
    private Connection connection;
    private PreparedStatement pst;
    private PreparedStatement pst2;

    public Asistent() {

        try {
            connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);


        } catch (SQLException d) {
            d.printStackTrace();
        }
            buttonDatePersonale.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rootPanel.removeAll();
                    rootPanel.add(panelDatePersonale);
                    rootPanel.repaint();
                    rootPanel.validate();

                    try{
                        pst = connection.prepareStatement("select * from adresa join date_personale on adresa.id = date_personale.id_adresa where date_personale.id = ?");
                        pst.setInt(1, Login_gui.idDatePersonale);
                        resultSet = pst.executeQuery();
                        pst2 = connection.prepareStatement("select * from angajat join utilizator on utilizator.id = angajat.id_utilizator where utilizator.id = ?");
                        pst2.setInt(1, Login_gui.id);
                        resultSet2 = pst2.executeQuery();
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
                    }
                    catch (SQLException d) {
                        d.printStackTrace();
                    }

                }
            });

        buttonRaportAnalize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                rootPanel.removeAll();
                rootPanel.add(panelRaportAnalize);
                rootPanel.repaint();
                rootPanel.validate();
                Vector<String> nimic = new Vector<>();
                nimic.add("");
                textFieldRaportCNP.setText("");
                listPentruRaportAnaliza.setListData(nimic);

                comboBox1.removeAllItems();

            }
        });
        buttonDeconectare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.mainPage.setContentPane(new Login_gui().getPanel());
                Main.mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Main.mainPage.pack();
                Main.mainPage.setVisible(true);
            }
        });
        buttonConfirmare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    try {
                        comboBox1.removeAllItems();
                        int ok = 0;
                        int validarePersoana = 0;
                        pst = connection.prepareStatement("select analiza_medicala.id,analiza_medicala.valid from analiza_medicala join istoric on istoric.id = analiza_medicala.id_istoric join pacient on pacient.id = istoric.id_pacient join date_personale on date_personale.id = pacient.id_date_personale where date_personale.cnp = ?;");
                        pst.setString(1,textFieldRaportCNP.getText());
                        resultSet = pst.executeQuery();
                        while (resultSet.next()) {
                            if(!resultSet.getBoolean("valid")) {
                                comboBox1.addItem(resultSet.getString("id"));
                                validarePersoana = 1;
                            }
                                ok = 1;

                        }
                        if(ok == 0)
                        {
                            JOptionPane.showMessageDialog(null,"Incorect CNP");
                        }
                        else
                        if(validarePersoana == 0)
                        {
                            JOptionPane.showMessageDialog(null,"Nu avem analize!");
                        }
                    } catch (SQLException d) {
                        d.printStackTrace();
                    }
                }


        });
        buttonValidare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeAsistent = null;
                String prenumeAsistent = null;
                String numeAsistent2 = null;
                String prenumeAsistent2 = null;
                try {
                    pst = connection.prepareStatement("select date_personale.nume, date_personale.prenume from adresa join date_personale on adresa.id = date_personale.id_adresa where date_personale.id = ?");
                    pst.setInt(1, Login_gui.idDatePersonale);
                    resultSet = pst.executeQuery();
                    while(resultSet.next())
                    {
                        numeAsistent = resultSet.getString("date_personale.nume");
                        prenumeAsistent = resultSet.getString("date_personale.prenume");
                    }
                }catch (SQLException d){
                    d.printStackTrace();
                }

                try {
                    pst = connection.prepareStatement("select analiza_medicala.nume_asistent,analiza_medicala.prenume_asistent from analiza_medicala join istoric on istoric.id = analiza_medicala.id_istoric join pacient on pacient.id = istoric.id_pacient join date_personale on date_personale.id = pacient.id_date_personale where date_personale.cnp = ? and analiza_medicala.id = ? ;");
                    pst.setString(1, textFieldRaportCNP.getText());
                    pst.setInt(2, Integer.parseInt(comboBox1.getSelectedItem().toString()));
                    resultSet = pst.executeQuery();
                    while(resultSet.next())
                    {
                        numeAsistent2 = resultSet.getString("analiza_medicala.nume_asistent");
                        prenumeAsistent2 = resultSet.getString("analiza_medicala.prenume_asistent");
                    }
                }catch (SQLException d){
                    d.printStackTrace();
                }


                if(numeAsistent.equals(numeAsistent2) && prenumeAsistent.equals(prenumeAsistent2))
                {
                    try {
                        pst = connection.prepareStatement("update analiza_medicala set valid = 1 where id = ?;");
                        pst.setInt(1, Integer.parseInt(Objects.requireNonNull(comboBox1.getSelectedItem()).toString()));
                        pst.execute();
                    }catch (SQLException d){
                        d.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null,"Analiza medicala a fost validata cu succes!");
                }
                else{
                    JOptionPane.showMessageDialog(null,"Aceasta analiza nu apartine de pacientul dumneavoastra!");
                }


                Vector<String> nimic = new Vector<>();
                nimic.add("");
                textFieldRaportCNP.setText("");
                listPentruRaportAnaliza.setListData(nimic);
                comboBox1.removeAllItems();
            }
        });
        adaugareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idAnaliza = 0;
                try {
                    pst = connection.prepareStatement("select istoric.id from istoric\n" +
                            "join pacient on pacient.id = istoric.id_pacient\n" +
                            "join date_personale on date_personale.id = pacient.id_date_personale\n" +
                            "where date_personale.nume = ? and date_personale.prenume = ?");
                    pst.setString(1, textFieldNumeAnaliza.getText());
                    pst.setString(2, textFieldPrenumeAnaliza.getText());
                    resultSet = pst.executeQuery();
                    while (resultSet.next())
                    {
                        idAnaliza = resultSet.getInt("istoric.id");
                    }
                }catch (SQLException d){
                    d.printStackTrace();
                }
                if(idAnaliza != 0 ){
                    String numeAsistent = null;
                    String prenumeAsistent = null;
                    try {
                        pst = connection.prepareStatement("select date_personale.nume, date_personale.prenume from adresa join date_personale on adresa.id = date_personale.id_adresa where date_personale.id = ?");
                        pst.setInt(1, Login_gui.idDatePersonale);
                        resultSet = pst.executeQuery();
                        while(resultSet.next())
                        {
                        numeAsistent = resultSet.getString("date_personale.nume");
                        prenumeAsistent = resultSet.getString("date_personale.prenume");
                        }
                    }catch (SQLException d){
                        d.printStackTrace();
                    }

                    try {
                        pst = connection.prepareStatement("insert into analiza_medicala (id_istoric,raport_analiza, rezultat_numeric, valid, nume_asistent, prenume_asistent) values (?,?,?,false,?,?)");
                        pst.setInt(1, idAnaliza);
                        pst.setInt(2, Integer.parseInt(comboBoxRaportAnaliza.getSelectedItem().toString()));
                        pst.setDouble(3,Double.parseDouble(textFieldRezultatNumeric.getText()));
                        pst.setString(4,numeAsistent);
                        pst.setString(5,prenumeAsistent);
                        pst.execute();
                    }catch (SQLException d){
                        d.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null,"Analiza a fost transmisa cu succes!");
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Nu exista aceasta persoana!");
                }
               textFieldNumeAnaliza.setText("");
                textFieldPrenumeAnaliza.setText("");
                textFieldRezultatNumeric.setText("");

            }


        });
        buttonCreareRaport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.removeAll();
                rootPanel.add(panelCreareRaport);
                rootPanel.repaint();
                rootPanel.validate();
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem() != null)
                {
                    try {

                        pst = connection.prepareStatement("select date_personale.nume, date_personale.prenume, analiza_medicala.rezultat_numeric, " +
                                "analiza_medicala.raport_analiza from analiza_medicala join " +
                                "istoric on analiza_medicala.id_istoric=istoric.id join pacient on pacient.id=istoric.id_pacient " +
                                "join date_personale on date_personale.id=pacient.id_date_personale where analiza_medicala.id = ?;");
                        int indexSelectat = 0;
                        indexSelectat = Integer.parseInt(comboBox1.getSelectedItem().toString());
                        pst.setInt(1, indexSelectat);
                        ResultSet resultSet = pst.executeQuery();

                        while (resultSet.next()) {
                            Vector<String> dateAnaliza = new Vector<>();
                            dateAnaliza.add(resultSet.getString("date_personale.nume"));
                            dateAnaliza.add(resultSet.getString("date_personale.prenume"));
                            dateAnaliza.add(resultSet.getString("analiza_medicala.raport_analiza"));
                            dateAnaliza.add(resultSet.getString("analiza_medicala.rezultat_numeric"));
                            listPentruRaportAnaliza.setListData(dateAnaliza);
                        }
                    } catch (SQLException d) {
                        d.printStackTrace();
                    }
                }


            }
        });
        buttonSalariuAngajat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldSalar.setText("");
                rootPanel.removeAll();
                rootPanel.add(panelSalar);
                rootPanel.repaint();
                rootPanel.revalidate();
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
    }


    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JButton getButtonDatePersonale() {
        return buttonDatePersonale;
    }

    public void setButtonDatePersonale(JButton buttonDatePersonale) {
        this.buttonDatePersonale = buttonDatePersonale;
    }

    public JButton getButtonRaportAnalize() {
        return buttonRaportAnalize;
    }

    public void setButtonRaportAnalize(JButton buttonRaportAnalize) {
        this.buttonRaportAnalize = buttonRaportAnalize;
    }

    public JPanel getPanelMeniu() {
        return panelMeniu;
    }

    public void setPanelMeniu(JPanel panelMeniu) {
        this.panelMeniu = panelMeniu;
    }

    public JPanel getPanelDatePersonale() {
        return rootPanel;
    }

    public void setPanelDatePersonale(JPanel panelDatePersonale) {
        rootPanel = panelDatePersonale;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JScrollPane getScrollPanePentruRaportAnaliza() {
        return scrollPanePentruRaportAnaliza;
    }

    public void setScrollPanePentruRaportAnaliza(JScrollPane scrollPanePentruRaportAnaliza) {
        this.scrollPanePentruRaportAnaliza = scrollPanePentruRaportAnaliza;
    }

    public JList getListPentruRaportAnaliza() {
        return listPentruRaportAnaliza;
    }

    public void setListPentruRaportAnaliza(JList listPentruRaportAnaliza) {
        this.listPentruRaportAnaliza = listPentruRaportAnaliza;
    }
}
