class BackupThread extends Thread {
    public void insertFilteredDataRow() throws SQLException {
        if (rightFtLines == 0) {
            insertFtStm = new StringBuilder("INSERT INTO " + filteredTableName + " (" + "f_ref_number, " + "f_date_time, " + "f_wpr_id," + "f_lang_id, " + "f_ns_id, " + "f_title, " + "f_action_id, " + "f_resp_time, " + "f_rm_id," + "f_md5_hash) VALUES \n");
        }
        String dbInsert = "( " + "'" + refNumber + "'," + "'" + dateTime + "', " + (projectDBCode.equals("NULL") ? projectDBCode : "'" + projectDBCode + "'") + ", " + (languageDBCode.equals("NULL") ? languageDBCode : "'" + languageDBCode + "'") + ", " + (nameSpaceDBCode.equals("NULL") ? nameSpaceDBCode : "'" + nameSpaceDBCode + "'") + ", " + "'" + title + "'," + (spActionDBCode.equals("NULL") ? spActionDBCode : "'" + spActionDBCode + "'") + "," + "'" + responseTime + "'," + (reqMethodDBCode.equals("NULL") ? reqMethodDBCode : "'" + reqMethodDBCode + "'") + ", ";
        String md5h = "NULL";
        if (!title.equals("NULL")) {
            md5hash.update(title.getBytes(), 0, title.length());
            md5h = new BigInteger(1, md5hash.digest()).toString(16);
            while (md5h.length() != 32) {
                md5h = "0" + md5h;
            }
            md5h = "'" + md5h + "'";
        }
        dbInsert += md5h + ")";
        rightFtLines++;
        insertFtStm.append(dbInsert);
        if (rightFtLines == numValInsert) {
            insertFtStm.append(';');
            if (mode == SquidLogFileProcessor.SIMUL) {
                System.out.println(insertFtStm.toString());
            } else if (mode == SquidLogFileProcessor.REAL) {
                stm.executeUpdate(insertFtStm.toString());
            }
            totalFtRightLines += rightFtLines;
            rightFtLines = 0;
        } else {
            insertFtStm.append(",\n ");
        }
        if (filter.getProjectFilteredItemDBCode(projectName, FiltrableItems.ACTION, "SEARCH").equals(spActionDBCode)) {
            insertSearch();
        }
    }
}
