class BackupThread extends Thread {
    @Override
    public void update(StudentsGroup obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("GROUP_TYPE_ID", obj.getGroupType().getId());
        newCrit.addCriterion("YEAR_STUDY_ID", obj.getYearOfStudy().getId());
        newCrit.addCriterion("STUDENT_GROUP_NAME", obj.getName());
        newCrit.addCriterion("STUDENT_GROUP_NB_STUDENT", obj.getNbStudents());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("STUDENT_GROUP_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(StudentsGroupDAO.TABLE_NAME, newCrit, critWhere).toString());
            stmt.getConnection().commit();
        } catch (SQLException e) {
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException(TABLE_NAME + " Rollback Exception :", e1);
            }
            throw new UpdateException(TABLE_NAME + " Update exception", e);
        }
    }
}
