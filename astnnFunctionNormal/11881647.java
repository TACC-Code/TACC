class BackupThread extends Thread {
    private void getChannels() throws DataException {
        try {
            chan1i = Double.parseDouble(text1.getText().trim());
            chan1f = Double.parseDouble(text2.getText().trim());
            chan2i = Double.parseDouble(text3.getText().trim());
            chan2f = Double.parseDouble(text4.getText().trim());
        } catch (NumberFormatException nfe) {
            throw new DataException("A Channel is not a valid number.", nfe);
        }
    }
}
