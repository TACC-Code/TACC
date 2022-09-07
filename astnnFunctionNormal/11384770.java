    public static String getPostgresqlDAOImplBaseFile(String packageName, Table table) {
        StringBuffer sb = new StringBuffer("");
        boolean first = true;
        ArrayList primaryKeys = null;
        ArrayList foreignKeys = null;
        sb.append("package " + packageName + ".jdbc;\n");
        sb.append("\n");
        sb.append("import " + packageName + ".dao.*;\n");
        sb.append("import " + packageName + ".dto.*;\n");
        sb.append("import " + packageName + ".exception.*;\n");
        sb.append("import java.util.*;\n");
        sb.append("import java.sql.*;\n");
class BackupThread extends Thread {
        sb.append("import org.apache.commons.logging.*;\n");
        sb.append("\n");
        sb.append("public class " + table.getJavaName() + "DaoImplBase " + "extends PostgresqlBase implements " + table.getJavaName() + "DaoBase {\n");
        sb.append("\n    private Integer limit;");
        sb.append("\n    private Integer offset;");
        sb.append("\n");
        ArrayList columns = table.getColumns();
        if (columns != null) {
            sb.append("\n    protected static final String SQL_SELECT = \"SELECT ");
            for (int i = 0; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                if (col != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(col.getCannonicalName());
                }
            }
            sb.append(" FROM " + table.getTableName() + " \";");
        }
        first = true;
        primaryKeys = table.getPrimaryKeys();
        if (primaryKeys != null && primaryKeys.size() > 0) {
            sb.append("\n    protected static final String SQL_MAX_ID = \"SELECT MAX(");
            Column col = (Column) primaryKeys.get(0);
            sb.append(col.getColumnName() + ") FROM ");
            sb.append(table.getTableName());
            if (primaryKeys.size() > 1) {
                sb.append(" WHERE ");
                for (int i = 1; i < primaryKeys.size(); i++) {
                    col = (Column) primaryKeys.get(i);
                    if (col != null) {
                        if (first) {
                            first = false;
                        } else {
                            sb.append(" AND ");
                        }
                        sb.append(col.getCannonicalName() + " = ? ");
                    }
                }
            }
            sb.append("\";");
        }
        first = true;
        primaryKeys = table.getPrimaryKeys();
        if (columns != null) {
            sb.append("\n    protected static final String SQL_INSERT = \"INSERT INTO ");
            sb.append(table.getTableName() + " (");
            int start = 1;
            if (primaryKeys == null) {
                start = 0;
            }
            for (int i = start; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                if (col != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(col.getColumnName());
                }
            }
            sb.append(") VALUES (");
            first = true;
            for (int i = start; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                if (col != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append("?");
                }
            }
            sb.append(")\";");
        }
        first = true;
        if (columns != null) {
            sb.append("\n    protected static final String SQL_INSERT_WITH_ID = \"INSERT INTO ");
            sb.append(table.getTableName() + " (");
            for (int i = 0; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                if (col != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(col.getColumnName());
                }
            }
            sb.append(") VALUES (");
            first = true;
            for (int i = 0; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                if (col != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append("?");
                }
            }
            sb.append(")\";");
        }
        first = true;
        if (columns != null) {
            sb.append("\n    protected static final String SQL_UPDATE = \"UPDATE ");
            sb.append(table.getTableName() + " SET ");
            for (int i = 0; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                if (col != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(col.getColumnName() + " = ?");
                }
            }
            sb.append(" WHERE ");
            primaryKeys = table.getPrimaryKeys();
            first = true;
            if (primaryKeys != null) {
                for (int i = 0; i < primaryKeys.size(); i++) {
                    Column col = (Column) primaryKeys.get(i);
                    if (first) {
                        first = false;
                    } else {
                        sb.append(" AND ");
                    }
                    sb.append(col.getCannonicalName() + " = ?");
                }
            }
            sb.append("\";");
        }
        first = true;
        if (columns != null) {
            sb.append("\n    protected static final String SQL_DELETE = \"DELETE FROM ");
            sb.append(table.getTableName() + " WHERE ");
            primaryKeys = table.getPrimaryKeys();
            first = true;
            if (primaryKeys != null) {
                for (int i = 0; i < primaryKeys.size(); i++) {
                    Column col = (Column) primaryKeys.get(i);
                    if (first) {
                        first = false;
                    } else {
                        sb.append(" AND ");
                    }
                    sb.append(col.getCannonicalName() + " = ?");
                }
            }
            sb.append("\";");
        }
        sb.append("\n");
        sb.append("\n    protected static final String ORDER_BY = \" ORDER BY \";");
        sb.append("\n");
        sb.append("\n    protected String[][] orderByColumns = null;");
        sb.append("\n");
        sb.append("\n    public void setOrderByColumn(String column) {");
        sb.append("\n        String[][] columns = new String[1][2];");
        sb.append("\n        columns[0][0] = column;");
        sb.append("\n        columns[0][1] = \" ASC \";");
        sb.append("\n        setOrderByColumns(columns);");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public void setOrderByColumn(String column, boolean descending) {");
        sb.append("\n        String[][] columns = new String[1][2];");
        sb.append("\n        columns[0][0] = column;");
        sb.append("\n        columns[0][1] = (descending) ? \" DESC \" : \" ASC \";");
        sb.append("\n        setOrderByColumns(columns);");
        sb.append("\n    }");
        sb.append("\n    public void setOrderByColumns(String[][] columns) {");
        sb.append("\n        this.orderByColumns = columns;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public String[][] getOrderByColumns() {");
        sb.append("\n        return this.orderByColumns;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public String getOrderByClause() {");
        sb.append("\n        if (orderByColumns == null || orderByColumns.length <= 0)");
        sb.append("\n            return \"\";");
        sb.append("\n        StringBuffer sb = new StringBuffer(ORDER_BY);");
        sb.append("\n        boolean first = true;");
        sb.append("\n        for (int i=0; i<orderByColumns.length; i++) {");
        sb.append("\n            if (!first) {");
        sb.append("\n                sb.append(\", \");");
        sb.append("\n            } else {");
        sb.append("\n                first = false;");
        sb.append("\n            }");
        sb.append("\n            sb.append(orderByColumns[i][0]);");
        sb.append("\n            if(orderByColumns[i][1] != null) {");
        sb.append("\n               sb.append(\" \");");
        sb.append("\n               sb.append(orderByColumns[i][1]);");
        sb.append("\n            }");
        sb.append("\n        }");
        sb.append("\n        sb.append(\" \");");
        sb.append("\n        return sb.toString();");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public void setLimit(Integer limit) {");
        sb.append("\n        this.limit = limit;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public void setOffset(Integer offset) {");
        sb.append("\n        this.offset = offset;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public  ");
        sb.append(table.getJavaName());
        sb.append("[] findAll() throws ");
        sb.append(table.getJavaName());
        sb.append("DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_SELECT;");
        sb.append("\n            sql += getOrderByClause();");
        sb.append("\n            if (limit != null && limit.intValue() > 0)");
        sb.append("\n                sql += \" LIMIT \" + limit;");
        sb.append("\n            if (offset != null && offset.intValue() > 0)");
        sb.append("\n                sql += \"offset \" + offset;");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            log.trace(\"SQL: \" + sql);");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            return fetchMultipleResults(rs);");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        if (primaryKeys != null) {
            sb.append("\n    public ");
            sb.append(table.getJavaName());
            sb.append("PK insert(");
            sb.append(table.getJavaName());
            sb.append(" dto) throws " + table.getJavaName() + "DaoException {");
            sb.append("\n        return insert(dto");
            if (primaryKeys != null) {
                for (int i = 0; i < primaryKeys.size(); i++) {
                    sb.append(", null");
                }
            }
            sb.append(");");
            sb.append("\n    }");
            sb.append("\n");
        }
        sb.append("\n    public  ");
        sb.append(table.getJavaName());
        sb.append("PK insert(");
        sb.append(table.getJavaName());
        sb.append(" dto");
        first = true;
        String insertWithPKClause = "";
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                if (!first) {
                    insertWithPKClause += " && ";
                } else {
                    first = false;
                }
                insertWithPKClause += col.getJavaName() + " == null ";
                sb.append(", ");
                sb.append(col.getJavaType());
                sb.append(" ").append(col.getJavaName());
            }
        } else {
            insertWithPKClause = "true";
        }
        sb.append(") throws " + table.getJavaName() + "DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n        " + table.getJavaName() + " idto = null;");
        sb.append("\n        " + table.getJavaName() + "PK pk = null;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_INSERT;");
        sb.append("\n            if (!(" + insertWithPKClause + "))");
        sb.append("\n                sql = SQL_INSERT_WITH_ID;");
        sb.append("\n            con = getConnection();");
        sb.append("\n            con.setAutoCommit(false);");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            int paramCount = 1;");
        for (int i = 0; i < columns.size(); i++) {
            Column col = (Column) columns.get(i);
            if (i == 0 && primaryKeys != null) {
                sb.append("\n            if (!(" + insertWithPKClause + "))");
                sb.append("\n                ps.set").append(FileUtils.getRSType(col.getDataType())).append("(paramCount++, ");
                sb.append(" dto.get");
                sb.append(col.getJavaNameCaps() + "()");
                sb.append(");");
            } else {
                if (col.getJavaType().equals("String") || col.getJavaType().equals("java.sql.Date") || col.getJavaType().equals("java.sql.Time") || col.getJavaType().equals("java.sql.Timestamp")) {
                    sb.append("\n            ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(paramCount++, ");
                    sb.append(" dto.get");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                } else if (col.getJavaType().equals("Boolean")) {
                    sb.append("\n            if (dto.is");
                    sb.append(col.getJavaNameCaps() + "() == null)");
                    sb.append("\n                ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(paramCount++, false);");
                    sb.append("\n            else");
                    sb.append("\n            ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(paramCount++, ");
                    sb.append(" dto.is");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                } else {
                    sb.append("\n            if (dto.get");
                    sb.append(col.getJavaNameCaps() + "() == null)");
                    sb.append("\n                ps.setNull");
                    sb.append("(paramCount++, ");
                    sb.append(col.getDataType());
                    sb.append(");");
                    sb.append("\n            else");
                    sb.append("\n                ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(paramCount++, ");
                    sb.append(" dto.get");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                }
            }
        }
        sb.append("\n            ps.executeUpdate();");
        sb.append("\n            log.trace(\"SQL: \" + sql);");
        sb.append("\n            if (" + insertWithPKClause + ") {");
        primaryKeys = table.getPrimaryKeys();
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column key = (Column) primaryKeys.get(i);
                if (key.getJavaType().equals("Long") || key.getJavaType().equals("Integer") && key.getFkColumnName() == null) {
                    if (key.getJavaType().equals("Long")) sb.append("\n                long c").append(key.getJavaNameCaps()).append(" = -1L;"); else if (key.getJavaType().equals("Integer")) sb.append("\n                int c").append(key.getJavaNameCaps()).append(" = -1;");
                    sb.append("\n                String sql").append(key.getJavaNameCaps());
                    sb.append(" = \"select currval('").append(table.getTableName());
                    sb.append("_").append(key.getColumnName()).append("_seq')\";");
                    sb.append("\n                PreparedStatement ps").append(key.getJavaNameCaps());
                    sb.append(" = con.prepareStatement(sql").append(key.getJavaNameCaps());
                    sb.append(");");
                    sb.append("\n                ResultSet rs").append(key.getJavaNameCaps());
                    sb.append(" = ps").append(key.getJavaNameCaps()).append(".executeQuery();");
                    sb.append("\n                while (rs").append(key.getJavaNameCaps()).append(".next())");
                    sb.append("\n                    c").append(key.getJavaNameCaps()).append(" = rs");
                    sb.append(key.getJavaNameCaps()).append(".get").append(FileUtils.getRSType(key.getDataType()));
                    sb.append("(1);");
                    sb.append("\n                dto.set").append(key.getJavaNameCaps()).append("(c");
                    sb.append(key.getJavaNameCaps()).append(");");
                }
            }
        }
        sb.append("\n            }");
        sb.append("\n            con.commit();");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.rollback();");
        sb.append("\n                    con.setAutoCommit(true);");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n                    return dto.createPK();");
        sb.append("\n        }\n");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public  int ");
        sb.append("update(");
        sb.append(table.getJavaName());
        sb.append("PK pk, ");
        sb.append(table.getJavaName());
        sb.append(" dto)");
        sb.append(" throws " + table.getJavaName() + "DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        int numRows = -1;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_UPDATE;");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        int paramCount = 1;
        for (int i = 0; i < columns.size(); i++) {
            Column col = (Column) columns.get(i);
            if (col.getJavaType().equals("Boolean")) {
                sb.append("\n            if (dto.is");
                sb.append(col.getJavaNameCaps() + "() == null)");
                sb.append("\n                ps.setNull");
                sb.append("(" + (paramCount) + ", " + col.getDataType() + ");");
                sb.append("\n            else");
                sb.append("\n            ps.set");
                sb.append(FileUtils.getRSType(col.getDataType()));
                sb.append("(" + (paramCount) + ", ");
                sb.append(" dto.is");
                sb.append(col.getJavaNameCaps() + "()");
                sb.append(");");
            } else {
                sb.append("\n            if (dto.get");
                sb.append(col.getJavaNameCaps() + "() == null)");
                sb.append("\n                ps.setNull");
                sb.append("(" + (paramCount) + ", " + col.getDataType() + ");");
                sb.append("\n            else");
                sb.append("\n                ps.set");
                sb.append(FileUtils.getRSType(col.getDataType()));
                sb.append("(" + (paramCount) + ", ");
                sb.append(" dto.get");
                sb.append(col.getJavaNameCaps() + "()");
                sb.append(");");
            }
            paramCount++;
        }
        primaryKeys = table.getPrimaryKeys();
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                if (col.getJavaType().equals("Boolean")) {
                    sb.append("\n            ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(" + (paramCount) + ", ");
                    sb.append(" pk.is");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                } else {
                    sb.append("\n            ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(" + (paramCount) + ", ");
                    sb.append(" pk.get");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                }
                paramCount++;
            }
        }
        sb.append("\n            numRows = ps.executeUpdate();");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                    if (ps != null)");
        sb.append("\n                        ps.close();");
        sb.append("\n                    if (con != null) {");
        sb.append("\n                        con.close();");
        sb.append("\n                    }");
        sb.append("\n                } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }");
        sb.append("\n        return numRows;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public  int ");
        sb.append("delete(");
        sb.append(table.getJavaName());
        sb.append("PK pk) throws " + table.getJavaName() + "DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        int numRows = -1;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_DELETE;");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        primaryKeys = table.getPrimaryKeys();
        first = true;
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                if (col.getJavaType().equals("Boolean")) {
                    sb.append("\n            ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(" + (i + 1) + ", ");
                    sb.append(" pk.is");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                } else {
                    sb.append("\n            ps.set");
                    sb.append(FileUtils.getRSType(col.getDataType()));
                    sb.append("(" + (i + 1) + ", ");
                    sb.append(" pk.get");
                    sb.append(col.getJavaNameCaps() + "()");
                    sb.append(");");
                }
            }
        }
        sb.append("\n            numRows = ps.executeUpdate();");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                    if (ps != null)");
        sb.append("\n                        ps.close();");
        sb.append("\n                    if (con != null) {");
        sb.append("\n                        con.close();");
        sb.append("\n                    }");
        sb.append("\n                } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }");
        sb.append("\n        return numRows;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    public  ");
        sb.append(table.getJavaName());
        sb.append(" findWhereOIDEquals(long oid)");
        sb.append(" throws ");
        sb.append(table.getJavaName());
        sb.append("DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_SELECT");
        sb.append(" + \" WHERE OID");
        sb.append(" = ?\";");
        sb.append("\n            sql += getOrderByClause();");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            ps.setLong(1, oid);");
        sb.append("\n            log.trace(\"SQL: \" + sql);");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            return fetchSingleResult(rs);");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        first = true;
        sb.append("\n    public  ");
        sb.append(table.getJavaName() + " findByPrimaryKey(");
        sb.append(table.getJavaName() + "PK pk) throws ");
        sb.append(table.getJavaName() + "DaoException {");
        sb.append("\n        return findByPrimaryKey(");
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append("pk.get" + col.getJavaNameCaps() + "()");
            }
        }
        sb.append(");");
        sb.append("\n    }");
        sb.append("\n");
        first = true;
        sb.append("\n    public  ");
        sb.append(table.getJavaName() + " findByPrimaryKey(");
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(col.getJavaType() + " " + col.getJavaName());
            }
        }
        sb.append(") throws ");
        sb.append(table.getJavaName() + "DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_SELECT");
        sb.append(" + \" WHERE ");
        first = true;
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                if (first) {
                    first = false;
                } else {
                    sb.append(" AND ");
                }
                sb.append(col.getCannonicalName() + " = ?");
            }
        }
        sb.append("\";");
        sb.append("\n            sql += getOrderByClause();");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column col = (Column) primaryKeys.get(i);
                sb.append("\n            ps.set");
                sb.append(FileUtils.getRSType(col.getDataType()));
                sb.append("(" + (i + 1) + ", " + col.getJavaName() + ");");
            }
        }
        sb.append("\n            log.trace(\"SQL: \" + sql);");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            return fetchSingleResult(rs);");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }");
        sb.append("\n");
        first = true;
        Hashtable fkt = table.getForeignKeyTables();
        if (fkt != null) {
            Enumeration e = fkt.keys();
            while (e.hasMoreElements()) {
                String tableName = (String) e.nextElement();
                foreignKeys = (ArrayList) fkt.get(tableName);
                sb.append("\n    public  ");
                sb.append(table.getJavaName() + "[] findBy");
                sb.append(table.createJavaName(tableName) + "(");
                sb.append(table.createJavaName(tableName) + "PK pk) ");
                sb.append("throws " + table.getJavaName() + "DaoException {");
                sb.append("\n        return findBy");
                sb.append(table.createJavaName(tableName) + "(");
                first = true;
                if (foreignKeys != null) {
                    for (int i = 0; i < foreignKeys.size(); i++) {
                        Column fk = (Column) foreignKeys.get(i);
                        if (first) {
                            first = false;
                        } else {
                            sb.append(", ");
                        }
                        sb.append("pk.get" + table.createJavaName(fk.getFkColumnName()) + "()");
                    }
                }
                sb.append(");");
                sb.append("\n    }\n");
                sb.append("\n");
                sb.append("\n    public  ");
                sb.append(table.getJavaName() + "[] findBy");
                sb.append(table.createJavaName(tableName) + "(");
                first = true;
                if (foreignKeys != null) {
                    for (int i = 0; i < foreignKeys.size(); i++) {
                        Column fk = (Column) foreignKeys.get(i);
                        if (first) {
                            first = false;
                        } else {
                            sb.append(", ");
                        }
                        sb.append(fk.getJavaType() + " " + fk.getJavaName());
                    }
                }
                sb.append(") throws " + table.getJavaName() + "DaoException {");
                sb.append("\n        Connection con = null;");
                sb.append("\n        PreparedStatement ps = null;");
                sb.append("\n        ResultSet rs = null;");
                sb.append("\n");
                sb.append("\n        try {");
                sb.append("\n            String sql = SQL_SELECT");
                sb.append(" + \" WHERE ");
                first = true;
                if (foreignKeys != null) {
                    for (int i = 0; i < foreignKeys.size(); i++) {
                        Column fk = (Column) foreignKeys.get(i);
                        if (first) {
                            first = false;
                        } else {
                            sb.append(" AND ");
                        }
                        sb.append(fk.getColumnName() + " = ?");
                    }
                }
                sb.append("\";");
                sb.append("\n            sql += getOrderByClause();");
                sb.append("\n            if (limit != null && limit.intValue() > 0)");
                sb.append("\n                sql += \" LIMIT \" + limit;");
                sb.append("\n            if (offset != null && offset.intValue() > 0)");
                sb.append("\n                sql += \" OFFSET \" + offset;");
                sb.append("\n            con = getConnection();");
                sb.append("\n            ps = con.prepareStatement(sql);");
                first = true;
                if (foreignKeys != null) {
                    for (int i = 0; i < foreignKeys.size(); i++) {
                        Column col = (Column) foreignKeys.get(i);
                        sb.append("\n            ps.set");
                        sb.append(FileUtils.getRSType(col.getDataType()));
                        sb.append("(" + (i + 1) + ", " + col.getJavaName() + ");");
                    }
                }
                sb.append("\n            log.trace(\"SQL: \" + sql);");
                sb.append("\n            rs = ps.executeQuery();");
                sb.append("\n            return fetchMultipleResults(rs);");
                sb.append("\n        } catch (SQLException e) {");
                sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
                sb.append("\n            throw new ");
                sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
                sb.append("\n        } catch (Exception e) {");
                sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
                sb.append("\n            throw new ");
                sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
                sb.append("\n        } finally {");
                sb.append("\n            try {");
                sb.append("\n                if (rs != null)");
                sb.append("\n                    rs.close();");
                sb.append("\n                if (ps != null)");
                sb.append("\n                    ps.close();");
                sb.append("\n                if (con != null) {");
                sb.append("\n                    con.close();");
                sb.append("\n                }");
                sb.append("\n            } catch (Exception e) {");
                sb.append("\n            }");
                sb.append("\n        }\n");
                sb.append("\n    }\n");
            }
        }
        sb.append("\n");
        if (columns != null && columns.size() > 0) {
            for (int i = 0; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                sb.append("\n    public  ");
                sb.append(table.getJavaName());
                sb.append("[] findWhere" + col.getJavaNameCaps() + "Equals(");
                sb.append(col.getJavaType() + " " + col.getJavaName() + ") throws ");
                sb.append(table.getJavaName());
                sb.append("DaoException {");
                sb.append("\n        Connection con = null;");
                sb.append("\n        PreparedStatement ps = null;");
                sb.append("\n        ResultSet rs = null;");
                sb.append("\n");
                sb.append("\n        try {");
                sb.append("\n            String sql = SQL_SELECT");
                sb.append(" + \" WHERE " + col.getCannonicalName());
                sb.append(" = ?\";");
                sb.append("\n            sql += getOrderByClause();");
                sb.append("\n            if (limit != null && limit.intValue() > 0)");
                sb.append("\n                sql += \" LIMIT \" + limit;");
                sb.append("\n            if (offset != null && offset.intValue() > 0)");
                sb.append("\n                sql += \" OFFSET \" + offset;");
                sb.append("\n            con = getConnection();");
                sb.append("\n            ps = con.prepareStatement(sql);");
                sb.append("\n            ps.set");
                sb.append(FileUtils.getRSType(col.getDataType()));
                sb.append("(1, " + col.getJavaName() + ");");
                sb.append("\n            log.trace(\"SQL: \" + sql);");
                sb.append("\n            rs = ps.executeQuery();");
                sb.append("\n            return fetchMultipleResults(rs);");
                sb.append("\n        } catch (SQLException e) {");
                sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
                sb.append("\n            throw new ");
                sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
                sb.append("\n        } catch (Exception e) {");
                sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
                sb.append("\n            throw new ");
                sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
                sb.append("\n        } finally {");
                sb.append("\n            try {");
                sb.append("\n                if (rs != null)");
                sb.append("\n                    rs.close();");
                sb.append("\n                if (ps != null)");
                sb.append("\n                    ps.close();");
                sb.append("\n                if (con != null) {");
                sb.append("\n                    con.close();");
                sb.append("\n                }");
                sb.append("\n            } catch (Exception e) {");
                sb.append("\n            }");
                sb.append("\n        }\n");
                sb.append("\n    }\n");
            }
        }
        sb.append("\n    public  ");
        sb.append("Object[][] findBySelect(String sql, Object[] sqlParams) throws ");
        sb.append(table.getJavaName() + "DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            con = getConnection();");
        sb.append("\n            if (limit != null && limit.intValue() > 0)");
        sb.append("\n                sql += \" LIMIT \" + limit;");
        sb.append("\n            if (offset != null && offset.intValue() > 0)");
        sb.append("\n                sql += \" OFFSET \" + offset;");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            if (sqlParams != null) {");
        sb.append("\n                for (int i=0; i<sqlParams.length; i++) {");
        sb.append("\n                    ps.setObject((i+1), sqlParams[i]);");
        sb.append("\n                }");
        sb.append("\n            }");
        sb.append("\n            log.trace(\"SQL: \" + sql);");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            ArrayList rows = new ArrayList();");
        sb.append("\n            ArrayList cols = new ArrayList();");
        sb.append("\n            int colCount = rs.getMetaData().getColumnCount();");
        sb.append("\n            while (rs.next()) {");
        sb.append("\n                for(int i=0; i<colCount; i++)");
        sb.append("\n                    cols.add(rs.getObject(i+1));");
        sb.append("\n                rows.add(cols.toArray(new Object[colCount]));");
        sb.append("\n                cols = new ArrayList();");
        sb.append("\n            }");
        sb.append("\n            Object[][] ra = (Object[][]) rows.toArray(new Object[rows.size()][colCount]);");
        sb.append("\n            return ra;");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        sb.append("\n");
        sb.append("\n    public ");
        sb.append(table.getJavaName() + "[] findByWhere(String where, Object[] sqlParams) throws ");
        sb.append(table.getJavaName() + "DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n");
        sb.append("\n        try {");
        sb.append("\n            String sql = SQL_SELECT + \" WHERE \" + where;");
        sb.append("\n                sql += getOrderByClause();");
        sb.append("\n            if (limit != null && limit.intValue() > 0)");
        sb.append("\n                sql += \" LIMIT \" + limit;");
        sb.append("\n            if (offset != null && offset.intValue() > 0)");
        sb.append("\n                sql += \" OFFSET \" + offset;");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            if (sqlParams != null) {");
        sb.append("\n                for (int i=0; i<sqlParams.length; i++) {");
        sb.append("\n                    ps.setObject((i+1), sqlParams[i]);");
        sb.append("\n                }");
        sb.append("\n            }");
        sb.append("\n            log.trace(\"SQL: \" + sql);");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            return fetchMultipleResults(rs);");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        sb.append("\n");
        sb.append("\n    public int countAll() throws ").append(table.getJavaName()).append("DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n        int count = 0;");
        sb.append("\n        try {");
        sb.append("\n            String sql = \"SELECT count(").append(((Column) table.getColumns().get(0)).getColumnName()).append(") from ").append(table.getTableName()).append("\";");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            while (rs.next())");
        sb.append("\n                count = rs.getInt(1);");
        sb.append("\n            return count;");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        sb.append("\n    public int countByPrimaryKey(").append(table.getJavaName()).append("PK pk) throws ").append(table.getJavaName()).append("DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n        int count = 0;");
        sb.append("\n        try {");
        sb.append("\n            String sql = \"SELECT count(").append(((Column) table.getColumns().get(0)).getColumnName()).append(") from ").append(table.getTableName());
        sb.append(" WHERE ");
        primaryKeys = table.getPrimaryKeys();
        first = true;
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column key = (Column) primaryKeys.get(i);
                if (!first) {
                    sb.append(" AND ");
                } else {
                    first = false;
                }
                sb.append(key.getColumnName()).append(" = ? ");
            }
        }
        sb.append("\";");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column key = (Column) primaryKeys.get(i);
                sb.append("\n            ps.set").append(FileUtils.getRSType(key.getDataType())).append("(").append((i + 1)).append(", pk.get").append(key.getJavaNameCaps()).append("());");
            }
        }
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            while (rs.next())");
        sb.append("\n                count = rs.getInt(1);");
        sb.append("\n            return count;");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        primaryKeys = table.getPrimaryKeys();
        sb.append("\n    public int countByPrimaryKey(");
        first = true;
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column key = (Column) primaryKeys.get(i);
                if (!first) {
                    sb.append(", ");
                } else {
                    first = false;
                }
                sb.append(key.getJavaType()).append(" ").append(key.getJavaName());
            }
        }
        sb.append(") throws ").append(table.getJavaName()).append("DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n        int count = 0;");
        sb.append("\n        try {");
        sb.append("\n            String sql = \"SELECT count(").append(((Column) table.getColumns().get(0)).getColumnName()).append(") from ").append(table.getTableName());
        sb.append(" WHERE ");
        primaryKeys = table.getPrimaryKeys();
        first = true;
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column key = (Column) primaryKeys.get(i);
                if (!first) {
                    sb.append(" AND ");
                } else {
                    first = false;
                }
                sb.append(key.getColumnName()).append(" = ? ");
            }
        }
        sb.append("\";");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        if (primaryKeys != null) {
            for (int i = 0; i < primaryKeys.size(); i++) {
                Column key = (Column) primaryKeys.get(i);
                sb.append("\n            ps.set").append(FileUtils.getRSType(key.getDataType())).append("(").append((i + 1)).append(", ").append(key.getJavaName()).append(");");
            }
        }
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            while (rs.next())");
        sb.append("\n                count = rs.getInt(1);");
        sb.append("\n            return count;");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column col = (Column) columns.get(i);
            sb.append("\n    public int countWhere").append(col.getJavaNameCaps()).append("Equals(").append(col.getJavaType()).append(" ").append(col.getJavaName());
            sb.append(") throws ").append(table.getJavaName()).append("DaoException {");
            sb.append("\n        Connection con = null;");
            sb.append("\n        PreparedStatement ps = null;");
            sb.append("\n        ResultSet rs = null;");
            sb.append("\n        int count = 0;");
            sb.append("\n        try {");
            sb.append("\n            String sql = \"SELECT count(").append(((Column) table.getColumns().get(0)).getColumnName()).append(") from ").append(table.getTableName());
            sb.append(" WHERE ");
            sb.append(col.getColumnName()).append(" = ? ");
            sb.append("\";");
            sb.append("\n            con = getConnection();");
            sb.append("\n            ps = con.prepareStatement(sql);");
            sb.append("\n            ps.set").append(FileUtils.getRSType(col.getDataType())).append("(1, ").append(col.getJavaName()).append(");");
            sb.append("\n            rs = ps.executeQuery();");
            sb.append("\n            while (rs.next())");
            sb.append("\n                count = rs.getInt(1);");
            sb.append("\n            return count;");
            sb.append("\n        } catch (SQLException e) {");
            sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
            sb.append("\n            throw new ");
            sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
            sb.append("\n        } catch (Exception e) {");
            sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
            sb.append("\n            throw new ");
            sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
            sb.append("\n        } finally {");
            sb.append("\n            try {");
            sb.append("\n                if (rs != null)");
            sb.append("\n                    rs.close();");
            sb.append("\n                if (ps != null)");
            sb.append("\n                    ps.close();");
            sb.append("\n                if (con != null) {");
            sb.append("\n                    con.close();");
            sb.append("\n                }");
            sb.append("\n            } catch (Exception e) {");
            sb.append("\n            }");
            sb.append("\n        }\n");
            sb.append("\n    }\n");
        }
        sb.append("\n    public int countByWhere(String where, Object[] sqlParams) throws ").append(table.getJavaName()).append("DaoException {");
        sb.append("\n        Connection con = null;");
        sb.append("\n        PreparedStatement ps = null;");
        sb.append("\n        ResultSet rs = null;");
        sb.append("\n        int count = 0;");
        sb.append("\n        try {");
        sb.append("\n            String sql = \"SELECT count(").append(((Column) table.getColumns().get(0)).getColumnName()).append(") from ").append(table.getTableName()).append(" \";");
        sb.append("\n            if (where != null)  ");
        sb.append("\n               sql += \" WHERE \" + where;");
        sb.append("\n            con = getConnection();");
        sb.append("\n            ps = con.prepareStatement(sql);");
        sb.append("\n            if (sqlParams != null) {");
        sb.append("\n               for (int i=0; i<sqlParams.length; i++) { ");
        sb.append("\n                   ps.setObject((i+1), sqlParams[i]); ");
        sb.append("\n                }");
        sb.append("\n            } ");
        sb.append("\n            rs = ps.executeQuery();");
        sb.append("\n            while (rs.next())");
        sb.append("\n                count = rs.getInt(1);");
        sb.append("\n            return count;");
        sb.append("\n        } catch (SQLException e) {");
        sb.append("\n            logger.error(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"SQLException: \" + e.getMessage(), e);");
        sb.append("\n        } catch (Exception e) {");
        sb.append("\n            logger.error(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n            throw new ");
        sb.append(table.getJavaName() + "DaoException(\"Exception: \" + e.getMessage(), e);");
        sb.append("\n        } finally {");
        sb.append("\n            try {");
        sb.append("\n                if (rs != null)");
        sb.append("\n                    rs.close();");
        sb.append("\n                if (ps != null)");
        sb.append("\n                    ps.close();");
        sb.append("\n                if (con != null) {");
        sb.append("\n                    con.close();");
        sb.append("\n                }");
        sb.append("\n            } catch (Exception e) {");
        sb.append("\n            }");
        sb.append("\n        }\n");
        sb.append("\n    }\n");
        sb.append("\n    protected  ");
        sb.append(table.getJavaName());
        sb.append("[] fetchMultipleResults(ResultSet rs) throws SQLException {");
        sb.append("\n        ArrayList results = new ArrayList();");
        sb.append("\n        while (rs.next()) {");
        sb.append("\n            " + table.getJavaName() + " dto = new ");
        sb.append(table.getJavaName() + "();");
        sb.append("\n            populateDto(dto, rs);");
        sb.append("\n            results.add(dto);");
        sb.append("\n        }\n        ");
        sb.append(table.getJavaName() + " retValue[] = new ");
        sb.append(table.getJavaName() + "[results.size()];");
        sb.append("\n        results.toArray(retValue);");
        sb.append("\n        return retValue;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n");
        sb.append("\n    protected  ");
        sb.append(table.getJavaName());
        sb.append(" fetchSingleResult(ResultSet rs) throws SQLException {");
        sb.append("\n        if (rs.next()) {");
        sb.append("\n            " + table.getJavaName() + " dto = new ");
        sb.append(table.getJavaName() + "();");
        sb.append("\n            populateDto(dto, rs);");
        sb.append("\n            return dto;");
        sb.append("\n        } else \n            return null;");
        sb.append("\n    }");
        sb.append("\n");
        sb.append("\n    protected static void populateDto(" + table.getJavaName() + " dto, ResultSet rs) throws SQLException {");
        if (columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                Column col = (Column) columns.get(i);
                sb.append("\n        try {");
                sb.append("\n            dto.set" + col.getJavaNameCaps());
                sb.append("(rs.get");
                sb.append(FileUtils.getRSType(col.getDataType()));
                sb.append("(COLUMN_POSITION_" + col.getColumnName().toUpperCase());
                sb.append("));");
                sb.append("\n            if (rs.wasNull())");
                sb.append("\n                dto.set").append(col.getJavaNameCaps());
                sb.append("(null);");
                sb.append("\n        } catch (Exception e) {}");
            }
        }
        sb.append("\n    }\n");
        sb.append("\n");
        sb.append("\n}\n");
        return sb.toString();
    }
}
