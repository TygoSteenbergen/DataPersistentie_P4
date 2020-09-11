package DAO.Reiziger;

import Model.Reiziger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        Statement st = conn.createStatement();
        String query = String.format(
                "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum)\n" +
                        "VALUES ('%s', '%s', '%s', '%s', '%s');"
                , reiziger.getId(), reiziger.getVoorletters(), reiziger.getTussenvoegsel()
                , reiziger.getAchternaam(), reiziger.getGeboortedatum());
        st.executeUpdate(query);
        st.close();
        return findById(reiziger.getId()).equals(reiziger);
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        Statement st = conn.createStatement();
        String query = String.format(
                "UPDATE reiziger\n" +
                        "SET voorletters = '%s', tussenvoegsel = '%s', achternaam = '%s', geboortedatum = '%s'\n" +
                        "WHERE reiziger_id = %s;"
                , reiziger.getVoorletters(), reiziger.getTussenvoegsel()
                , reiziger.getAchternaam(), reiziger.getGeboortedatum(), reiziger.getId());
        st.executeUpdate(query);
        return findById(reiziger.getId()).equals(reiziger);
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        Statement st = conn.createStatement();
        String query = String.format(
                "DELETE FROM reiziger where reiziger_id = %s"
                , reiziger.getId());
        st.executeUpdate(query);
        st.close();
        return findById(reiziger.getId()) == null;
    }

    @Override
    public Reiziger findById(int id) {
        try {
            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM reiziger WHERE reiziger_id = '%s';", id);
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                return new Reiziger(rs.getInt(1), rs.getString(2), rs.getString(3) != null ? rs.getString(3) : "",
                        rs.getString(4), rs.getDate(5));
            }else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        try {
            List<Reiziger> reizigers = new ArrayList<>();
            Statement st = conn.createStatement();
            String query = String.format("SELECT * FROM reiziger WHERE geboortedatum = '%s';", datum);
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3) != null ? rs.getString(3) : "",
                        rs.getString(4), rs.getDate(5));
                reizigers.add(reiziger);
            }
            return reizigers;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll(){
        try {
            List<Reiziger> reizigers = new ArrayList<>();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM reiziger;");
            System.out.println("Alle reizigers:");
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3) != null ? rs.getString(3) : "",
                        rs.getString(4), rs.getDate(5));
                reizigers.add(reiziger);
            }
            rs.close();
            st.close();
            return reizigers;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(8035, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        List<Reiziger> reizigersMetGbDatum = rdao.findByGbdatum("1981-03-14");
        System.out.println("[Test] ReizigerDAO.findByGbDatum('1981-03-14') geeft de volgende reizigers:");
        for (Reiziger r : reizigersMetGbDatum) {
            System.out.println(r);
        }
        System.out.println();

        System.out.println("[Test] ReizigerDAO.findById(reiziger.getId()) geeft de volgende reiziger: \n" + rdao.findById(sietske.getId()));
        System.out.println();

        sietske.setAchternaam("Bakker");
        rdao.update(sietske);
        System.out.println("[Test] Na enige aanpassingen aan de reiziger en het aanroepen van ReizigerDAO.update(reiziger)" +
                " geeft ReizigerDAO.findById(reiziger.getId()) de volgende reiziger: \n" + rdao.findById(sietske.getId()));
        System.out.println();

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

    }

}
