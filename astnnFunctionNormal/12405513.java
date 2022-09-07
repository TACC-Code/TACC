class BackupThread extends Thread {
    public int saveProduct() throws SQLException {
        Connection connection = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int value = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(jdbcURL);
            connection.setAutoCommit(false);
            String query1 = "INSERT INTO products(name,description) VALUES(?,?)";
            ps1 = (PreparedStatement) connection.prepareStatement(query1);
            ps1.setString(1, this.name);
            ps1.setString(2, this.description);
            ps1.executeUpdate();
            String query2 = "SELECT MAX(product_id) AS ProductId FROM products";
            Statement s = (Statement) connection.createStatement();
            s.executeQuery(query2);
            rs = s.getResultSet();
            while (rs.next()) {
                value = rs.getInt("productId");
                break;
            }
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
            }
            try {
                ps1.close();
            } catch (Exception ex) {
            }
        }
        return value;
    }
}
