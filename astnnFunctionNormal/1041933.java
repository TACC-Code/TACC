class BackupThread extends Thread {
    public void compareResponsesToInterventions() {
        Spreadsheet s = new Spreadsheet(new File("testspreadsheet.csv"));
        s.addNewColumnOfContiguousTurns();
        s.addInterventionTXTColumn();
        s.addTSTColumn();
        s.addInterventionLexicalMatchingScores();
        File output = new File("testspreadsheetout.csv");
        s.writeSpreadsheetToFile(output);
    }
}
