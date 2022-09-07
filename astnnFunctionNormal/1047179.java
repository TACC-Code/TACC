class BackupThread extends Thread {
    public void testWriteSerializedObjectToFile() {
        final Date birthdayFromNiko = DateUtils.createDate(2007, 11, 8);
        final File writeInMe = new File(this.deepDir, "testWriteSerializedObjectToFile.dat");
        this.result = SerializedObjectUtils.writeSerializedObjectToFile(birthdayFromNiko, writeInMe);
        assertTrue("", this.result);
        final Object readedObjectFromFile = SerializedObjectUtils.readSerializedObjectFromFile(writeInMe);
        final Date readedObj = (Date) readedObjectFromFile;
        this.result = birthdayFromNiko.equals(readedObj);
        assertTrue("", this.result);
    }
}
