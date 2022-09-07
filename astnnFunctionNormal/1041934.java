class BackupThread extends Thread {
    public void compareContiguousTurns() {
        Spreadsheet s = new Spreadsheet(new File("testspreadsheet.csv"));
        s.addNewColumnOfContiguousTurns();
        s.testThatEachRowIsOfADifferentParticipant();
        s.compareWholeSpreadsheet();
        File output = new File("testspreadsheetout.csv");
        s.writeSpreadsheetToFile(output);
    }
}
