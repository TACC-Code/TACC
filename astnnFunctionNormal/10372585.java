class BackupThread extends Thread {
    public void testGetRowContentForSpecificSheet() throws Exception {
        String[][] write = new String[][] { { "00", "01", "02" }, { "10" }, {}, { "30", "31", "32", "33", "34", "35", "36", "37", "38", "39" }, { "40", "41" }, { "50", "51", "52", "53", "54", "55", "56", "57", "58" }, { "60", "61", "62", "63", "64", "65", "66" }, {}, { "80", "81", "82", "83", "84", "85", "86", "87", "88", "89" } };
        setExcelFileContent(write, sheetName);
        sleep(5000);
        ExcelFile excelFile = ExcelFile.getInstance(fileName, true);
        StringBuffer sbWrite;
        StringBuffer sbRead;
        for (int i = 0; i < write.length; i++) {
            sbWrite = new StringBuffer();
            sbRead = new StringBuffer();
            String[] read = excelFile.getRowContent(sheetName, i);
            for (int j = 0; j < write[i].length; j++) {
                sbWrite.append(write[i][j] + ";");
                sbRead.append(read[j] + ";");
            }
            report.report("Row No." + i + " : write : " + sbWrite.toString() + ", read: " + sbRead.toString(), (sbWrite.toString().compareTo(sbRead.toString()) == 0));
        }
    }
}
