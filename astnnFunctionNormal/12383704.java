class BackupThread extends Thread {
    void go() {
        String algorithm = this.myAlgorithm.getSelectedItem().toString();
        clsPrefs.put(ALGORITHM, this.myAlgorithm.getSelectedItem().toString());
        String fileName = this.myFileName.getText();
        File fileToHash = new File(fileName);
        if (!fileToHash.exists()) {
            JOptionPane.showMessageDialog(this, "Could not access file", "Problem", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.myResults.setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            int totalBytesRead = 0;
            byte[] input = new byte[1000000];
            InputStream is = new FileInputStream(fileToHash);
            int bytesRead = is.read(input);
            if (bytesRead < input.length) {
                totalBytesRead = +bytesRead;
            } else {
                totalBytesRead = totalBytesRead + bytesRead;
            }
            while (bytesRead == input.length) {
                md.update(input);
                bytesRead = is.read(input);
                totalBytesRead = totalBytesRead + bytesRead;
            }
            byte[] finalInput = new byte[bytesRead];
            for (int i = 0; i < bytesRead; i++) {
                finalInput[i] = input[i];
            }
            md.update(finalInput);
            is.close();
            System.out.println("fileToHash.length(): " + fileToHash.length());
            System.out.println("totalBytesRead:      " + totalBytesRead);
            if (fileToHash.length() != totalBytesRead) {
                JOptionPane.showMessageDialog(this, "Could not read entire file", "Problem Encountered", JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte[] theHashAsBytes = md.digest();
            String hexHash = asHex(theHashAsBytes);
            this.myComputedHash.setText(hexHash);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Problem Encountered", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.myDownloadedHash.getText().trim().length() > 0) {
            checkHashes();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
