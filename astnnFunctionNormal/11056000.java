class BackupThread extends Thread {
    public boolean addVector(String patientDocument) {
        boolean ret = true;
        DBHelper helper = null;
        PreparedStatement psVector = null;
        PreparedStatement psVectorDetail = null;
        PreparedStatement psMedication = null;
        PreparedStatement beginTransaction = null;
        PreparedStatement commitTransaction = null;
        PreparedStatement rollbackTransaction = null;
        PreparedStatement psAdmit = null;
        PreparedStatement psWard = null;
        PreparedStatement psHospital = null;
        ResultSet rsHospital = null;
        try {
            helper = new DBHelper();
            PatientDocument doc = PatientDocument.Factory.parse(patientDocument);
            beginTransaction = helper.prepareStatement(SQL.begin());
            commitTransaction = helper.prepareStatement(SQL.commit());
            rollbackTransaction = helper.prepareStatement(SQL.rollback());
            beginTransaction.executeUpdate();
            psVector = helper.prepareStatement(SQL.insertVector());
            psVectorDetail = helper.prepareStatement(SQL.insertVectorDetail());
            psMedication = helper.prepareStatement(SQL.insertPatientMedication());
            psAdmit = helper.prepareStatement(SQL.insertAdmitance());
            psWard = helper.prepareStatement(SQL.insertPatientWard());
            psHospital = helper.prepareStatement(SQL.getHospitalFromWard());
            boolean isAdmited = false;
            for (AdmitanceType a : doc.getPatient().getAdmitanceRecord().getAdmitanceArray()) {
                if (a.getDateDischarged() == 0) {
                    isAdmited = true;
                }
            }
            if (!isAdmited) {
                long dateadmited = System.currentTimeMillis();
                String wardno = doc.getPatient().getWardHistory().getPatientInWardArray(0).getWardno();
                psHospital.setString(1, wardno);
                rsHospital = psHospital.executeQuery();
                rsHospital.next();
                String hospitalno = rsHospital.getString("HOSPITALNO");
                psAdmit.setString(1, MedisisKeyGenerator.generate());
                psAdmit.setString(2, hospitalno);
                psAdmit.setString(3, doc.getPatient().getPatientno());
                psAdmit.setLong(4, dateadmited);
                psWard.setString(1, MedisisKeyGenerator.generate());
                psWard.setString(2, doc.getPatient().getPatientno());
                psWard.setString(3, doc.getPatient().getWardHistory().getPatientInWardArray(0).getWardno());
                psWard.setLong(4, dateadmited);
                psWard.setLong(5, 0);
            }
            for (VectorType v : doc.getPatient().getVectors().getVectorArray()) {
                v.setVectorNo(MedisisKeyGenerator.generate());
                psVector.setString(1, v.getVectorNo());
                psVector.setString(2, doc.getPatient().getPatientno());
                psVector.setLong(3, System.currentTimeMillis());
                psVector.setString(4, v.getNotes());
                psVector.setLong(5, new Long(v.getGaf()));
                psVector.setString(6, v.getUserno());
                psVector.addBatch();
                for (SymptomRatingType ss : v.getSymptomRatingList().getRatingArray()) {
                    psVectorDetail.clearParameters();
                    psVectorDetail.setString(1, v.getVectorNo());
                    psVectorDetail.setString(2, ss.getSymptomNo());
                    psVectorDetail.setInt(3, ss.getValue().intValue());
                    psVectorDetail.addBatch();
                }
                for (MedicationType m : v.getMedicationList().getMedicationArray()) {
                    psMedication.clearParameters();
                    psMedication.setString(1, v.getVectorNo());
                    psMedication.setString(2, doc.getPatient().getPatientno());
                    psMedication.setString(3, m.getMedno());
                    psMedication.setString(4, m.getDose());
                    psMedication.setLong(5, System.currentTimeMillis());
                    psMedication.setString(6, v.getUserno());
                    psMedication.addBatch();
                }
            }
            if (!isAdmited) {
                psAdmit.executeUpdate();
                psWard.executeUpdate();
            }
            psVector.executeBatch();
            psVectorDetail.executeBatch();
            psMedication.executeBatch();
            commitTransaction.execute();
        } catch (Exception e) {
            ret = false;
            e.printStackTrace();
            try {
                rollbackTransaction.executeUpdate();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        } finally {
            try {
                if (rsHospital != null) {
                    rsHospital.close();
                }
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
