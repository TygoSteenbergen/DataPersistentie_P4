import DAO.Adres.AdresDAO;
import DAO.Adres.AdresDAOPsql;
import DAO.Reiziger.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip?user=postgres&password=Dinglebugger";
        try {
            Connection conn = DriverManager.getConnection(url);
            ReizigerDAOPsql RDOA = new ReizigerDAOPsql(conn);
//            ReizigerDAOPsql.testReizigerDAO(RDOA);
            AdresDAO ADAO = new AdresDAOPsql(conn, RDOA);
//            String gbdatum = "1981-03-14";
//            Reiziger sietske = new Reiziger(43537, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
//            RDOA.save(sietske);
//            Adres adres = new Adres(5012,"68","steenlaan", "Utrecht", 43537, "2965");
//            ADAO.save(adres);
            AdresDAOPsql.testAdresDAO(ADAO, RDOA);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}