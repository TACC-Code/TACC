class BackupThread extends Thread {
    public boolean insertMedicationList(String medicationListDocument) {
        boolean ret = false;
        PreparedStatement psRollback = null;
        try {
            MedicationListDocument doc = MedicationListDocument.Factory.parse(medicationListDocument);
            helper = new DBHelper();
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psInsert = helper.prepareStatement(SQL.insertMedication());
            psRollback = helper.prepareStatement(SQL.rollback());
            for (int i = 0; i < doc.getMedicationList().getMedicationArray().length; i++) {
                doc.getMedicationList().getMedicationArray(i).setMedno(MedisisKeyGenerator.generate());
                psInsert.setString(1, doc.getMedicationList().getMedicationArray(i).getMedno());
                psInsert.setString(2, doc.getMedicationList().getMedicationArray(i).getMedication());
                psInsert.addBatch();
            }
            psBegin.executeUpdate();
            psInsert.executeBatch();
            psCommit.executeUpdate();
            ret = true;
        } catch (Exception e) {
            try {
                if (psRollback != null) {
                    psRollback.executeUpdate();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
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
