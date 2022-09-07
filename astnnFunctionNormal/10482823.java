class BackupThread extends Thread {
    @Override
    public StudentsGroup store(StudentsGroup obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getGroupType().getId() == null || obj.getYearOfStudy().getId() == null) {
            throw new InsertException("Missing GroupType FK and YearOfStudy FK");
        } else {
            Statement stmt = OracleJDBConnector.getInstance().getStatement();
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            values.add(obj.getGroupType().getId());
            values.add(obj.getYearOfStudy().getId());
            values.add(obj.getName());
            values.add(obj.getNbStudents());
            try {
                stmt.executeUpdate(new InsertQuery(StudentsGroupDAO.TABLE_NAME, values).toString());
                Criteria critWhere = new Criteria();
                critWhere.addCriterion("YEAR_STUDY_ID", obj.getName());
                critWhere.addCriterion("STUDENT_GROUP_NAME", obj.getName());
                List<SQLWord> listSelect = new ArrayList<SQLWord>();
                listSelect.add(new SQLWord("STUDENT_GROUP_ID"));
                ResultSet result = stmt.executeQuery(new SelectQuery(StudentsGroupDAO.TABLE_NAME, listSelect, critWhere).toString());
                if (result != null) {
                    while (result.next()) obj.setId(result.getInt(1));
                } else {
                    throw new SelectException(TABLE_NAME + " Can't retieve record");
                }
                stmt.getConnection().commit();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    stmt.getConnection().rollback();
                } catch (SQLException e1) {
                    throw new DBConnectionException("Rollback Exception :", e1);
                }
                throw new InsertException(TABLE_NAME + " Insert Exception :", e);
            }
        }
        return obj;
    }
}
