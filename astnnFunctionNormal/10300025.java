class BackupThread extends Thread {
    public void insertSearch() {
        if (searchLines == 0) {
            searchFtStm = new StringBuilder("INSERT INTO " + searchesTableName + " (" + "f_ref_number, " + "f_date_time, " + "f_lang_id, " + "f_search, " + "f_md5_hash) VALUES \n");
        }
        String dbInsert = "( " + "'" + refNumber + "'," + "'" + dateTime + "', " + "'" + language + "', " + (searched.equals("NULL") ? searched : "'" + searched + "'") + ",";
        String md5h = "NULL";
        if (!searched.equals("NULL")) {
            md5hash.update(searched.getBytes(), 0, searched.length());
            md5h = new BigInteger(1, md5hash.digest()).toString(16);
            while (md5h.length() != 32) {
                md5h = "0" + md5h;
            }
            md5h = "'" + md5h + "'";
        }
        dbInsert += md5h + ")";
        searchFtStm.append(dbInsert);
        searchLines++;
        if (searchLines == 5) {
            searchFtStm.append(';');
            if (mode == SquidLogFileProcessor.SIMUL) {
                System.out.println(searchFtStm.toString());
            } else if (mode == SquidLogFileProcessor.REAL) {
                try {
                    stm.executeUpdate(searchFtStm.toString());
                } catch (SQLException sql) {
                    parsingLog.println("insertSearched: Error Inserting searched row");
                    parsingLog.println(sql.getMessage());
                    parsingLog.println("Sentence: " + searchFtStm);
                } finally {
                    searchLines = 0;
                }
            }
        } else {
            searchFtStm.append(",\n ");
        }
    }
}
