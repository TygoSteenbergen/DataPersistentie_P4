package DAO.Adres;

import DAO.Reiziger.ReizigerDAO;
import Model.Adres;
import Model.Reiziger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAO reizigerDAO) {
        this.conn = conn;
        rdao = reizigerDAO;
    }


    @Override
    public boolean save(Adres adres) {
        try {
            Statement st = conn.createStatement();
            String query = String.format(
                    "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id)\n" +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s');"
                    , adres.getId(), adres.getPostcode(), adres.getHuisnummer(), adres.getStraat()
                    , adres.getWoonplaats(), adres.getReizigerId());
            st.executeUpdate(query);
            st.close();
//            return findByReiziger(rdao.findById(adres.getReizigerId())).equals(adres);
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            Statement st = conn.createStatement();
            String query = String.format(
                    "UPDATE adres\n" +
                            "SET postcode= '%s', huisnummer = '%s', straat = '%s', woonplaats = '%s', reiziger_id = '%s'\n" +
                            "WHERE adres_id = %s;"
                    , adres.getPostcode(), adres.getHuisnummer(), adres.getStraat()
                    , adres.getWoonplaats(), adres.getReizigerId(), adres.getId());
            st.executeUpdate(query);
            return findByReiziger(rdao.findById(adres.getReizigerId())).equals(adres);
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            Statement st = conn.createStatement();
            String query = String.format(
                    "DELETE FROM adres where adres_id = %s"
                    , adres.getId());
            st.executeUpdate(query);
            st.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        Statement st = conn.createStatement();
        String query = String.format("SELECT * FROM adres WHERE reiziger_id = '%s';", reiziger.getId());
        System.out.println(reiziger.getId());
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            return new Adres(rs.getInt(1),
                    rs.getString(3), rs.getString(4), rs.getString(5)
                    , rs.getInt(6), rs.getString(2));
        } else {
            return null;
        }
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM adres;");
        while (rs.next()) {
            Adres adres = new Adres(rs.getInt(1),
                    rs.getString(3), rs.getString(4), rs.getString(5)
                    , rs.getInt(6), rs.getString(2));
            adressen.add(adres);
        }
        rs.close();
        st.close();
        return adressen;

    }

    public static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        System.out.println();
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(51, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(sietske);
        Adres adres = new Adres(71, "63", "steenlaan", "Utrecht", sietske.getId(), "2965BD");
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(adres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");
        System.out.println();

        System.out.println("[Test] AdresDAO.findByReiziger(sietske) geeft het volgende adres: \n" + adao.findByReiziger(sietske));
        System.out.println();

        adres.setHuisnummer("67");
        adao.update(adres);
        System.out.println("[Test] Na enige aanpassingen aan het adres en het aanroepen van AdresDAO.update(adres)" +
                " geeft AdresDAO.findByReiziger(sietske) het volgende adres: \n" + adao.findByReiziger(sietske));
        System.out.println();

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(adres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " reizigers\n");

    }
}
