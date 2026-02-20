package koscom.infra.langgraphj.io.db;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MySqlClient implements DbClient {

    private final String jdbcUrl;   // 예: jdbc:mysql://localhost:3307/langgraph?serverTimezone=Asia/Seoul&useSSL=false
    private final String user;      // languser
    private final String password;  // langpw

    public MySqlClient(String jdbcUrl, String user, String password) {
        this.jdbcUrl = Objects.requireNonNull(jdbcUrl);
        this.user = Objects.requireNonNull(user);
        this.password = Objects.requireNonNull(password);
    }

    @Override
    public List<Doc> executeQuery(String sql, List<Object> params) throws Exception {
        Objects.requireNonNull(sql, "sql");

        List<Doc> docs = new ArrayList<>();

        // ✅ MySQL JDBC Driver는 classpath에 있으면 자동 로딩됨
        // (필요하면: Class.forName("com.mysql.cj.jdbc.Driver");)

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    docs.add(mapRowToDoc(rs));
                }
            }
        }
        System.out.println("[SQL]: " + sql + ", " + params);
        printLog(docs);

        return docs;
    }

    private void printLog(List<Doc> docs) {
        StringBuilder sb = new StringBuilder();

        sb.append("[RESULT EXECUTE QUERY]: ").append("\n");
        for(Doc doc : docs) {
            sb.append(doc.toString());
            sb.append("\n");
        }

        System.out.println(sb);
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        if (params == null) return;

        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);

            // 필요한 타입만 명시적으로 처리하고 나머지는 setObject로 처리
            if (p instanceof LocalDate) {
                ps.setDate(i + 1, Date.valueOf((LocalDate) p));
            } else {
                ps.setObject(i + 1, p);
            }
        }
    }

    private Doc mapRowToDoc(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String content = rs.getString("content");

        Date from = rs.getDate("from_date");
        Date to = rs.getDate("to_date");

        LocalDate fromDate = (from != null) ? from.toLocalDate() : null;
        LocalDate toDate = (to != null) ? to.toLocalDate() : null;

        String manager = rs.getString("manager");

        return new Doc(id, title, content, fromDate, toDate, manager);
    }
}