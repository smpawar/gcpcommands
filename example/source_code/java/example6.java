package com.example.example6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

final class Example6 {

  private Example6(String str1, String str2, Date startDate) {
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                        conn = databaseConnectionFactory.getDBConnection();

                        StringBuffer sbStmt = new StringBuffer()
                                        .append("select ").append(str1).append(",")
                                        .append(SELECT_WITH_GROUP_BY_STMT)
                                        .append("group by ").append(str1).append(" ")
                                        .append("order by cnt desc");
                        ps = conn.prepareStatement(sbStmt.toString());
                        int idx = 1;
                        ps.setString(idx++, sdf.format(startDate));
                        ps.setString(idx++, str2);

                        rs = ps.executeQuery();
                        while (rs.next()) {
                                idx = 1;
                                String status = rs.getString(idx++);
                                Long count = Long.valueOf(rs.getLong(idx++));
                                resultMap.put(status, count);
                        }
                } catch (Exception ex) {
                        logger.error("example6: Exception caught when querying database.", ex);
                } finally {
                        closeConnection(conn, ps, rs);
                }

                return resultMap;
  }
}
