package DAO.OVChipkaart;

import DAO.Reiziger.ReizigerDAO;
import Model.Adres;
import Model.OVChipkaart;
import Model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id )\n" +
                        "VALUES (?, ?, ?, ?, ?);");
        pst.setInt(1, ovChipkaart.getKaartNummer());
        pst.setDate(2, (Date) ovChipkaart.getGeldigTot());
        pst.setInt(3, ovChipkaart.getKlasse());
        pst.setDouble(4, ovChipkaart.getSaldo());
        pst.setInt(5, ovChipkaart.getReizigerId());
        pst.executeUpdate();
        pst.close();
        return findByReiziger(ovChipkaart.getReizigerId()).equals(ovChipkaart);
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE ov_chipkaart\n" +
                    "SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ?\n" +
                    "WHERE kaart_nummer = ? ;");
            pst.setDate(1, (Date) ovChipkaart.getGeldigTot());
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReizigerId());
            pst.setInt(5, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
            return findByReiziger(ovChipkaart.getReizigerId()).equals(ovChipkaart);
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("DELETE FROM ov_chipkaart where kaart_nummer = ?");
        pst.setInt(1, ovChipkaart.getKaartNummer());
        pst.executeUpdate();
        pst.close();
        return true;
    }

    @Override
    public OVChipkaart findByReiziger(int id) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?;");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new OVChipkaart(rs.getInt(1),
                    rs.getDate(2), rs.getInt(3), rs.getDouble(4)
                    , rs.getInt(5));
        } else {
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM adres;");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt(1),
                    rs.getDate(2), rs.getInt(3), rs.getDouble(4)
                    , rs.getInt(5));
        }
        rs.close();
        pst.close();
        return ovChipkaarten;

    }
}
