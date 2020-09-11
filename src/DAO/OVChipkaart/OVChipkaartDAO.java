package DAO.OVChipkaart;

import Model.OVChipkaart;
import Model.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart) throws SQLException;
    boolean update(OVChipkaart ovChipkaart) throws SQLException;
    boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    OVChipkaart findByReiziger(int id) throws SQLException;
    List<OVChipkaart> findAll() throws SQLException;
}
