class BackupThread extends Thread {
    public boolean insertSymptoms(String symptomListDocument) {
        boolean ret = true;
        PreparedStatement psSymptom = null;
        PreparedStatement psStart = null;
        PreparedStatement psCommit = null;
        PreparedStatement psRollback = null;
        SymptomListDocument doc = null;
        try {
            helper = new DBHelper();
            doc = SymptomListDocument.Factory.parse(symptomListDocument);
            psStart = helper.prepareStatement(SQL.begin());
            psCommit = helper.prepareStatement(SQL.commit());
            psRollback = helper.prepareStatement(SQL.rollback());
            psSymptom = helper.prepareStatement(SQL.insertSymptom());
            psStart.executeUpdate();
            for (int i = 0; i < doc.getSymptomList().getSymptomArray().length; i++) {
                Symptom symptom = doc.getSymptomList().getSymptomArray(i);
                symptom.setSymptomno(MedisisKeyGenerator.generate());
                psSymptom.setString(1, symptom.getSymptomno());
                psSymptom.setString(2, symptom.getSymptom());
                psSymptom.setString(3, symptom.getDsm());
                psSymptom.addBatch();
            }
            psSymptom.executeBatch();
            psCommit.executeUpdate();
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
            try {
                psRollback.executeUpdate();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } finally {
            try {
                if (helper != null) {
                    helper.cleanup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
