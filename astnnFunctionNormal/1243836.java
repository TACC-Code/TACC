class BackupThread extends Thread {
    public String[] getFieldList() {
        String[] fieldList = new String[fields.length - 1];
        for (int i = 0; i < fieldList.length; i++) {
            fieldList[i] = fields[i + 1];
        }
        return fieldList;
    }
}
