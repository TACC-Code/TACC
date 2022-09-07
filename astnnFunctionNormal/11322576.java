class BackupThread extends Thread {
    private String nextLine() {
        try {
            String tmpStr = d.readLine();
            debug("nextLine: tmpStr : " + tmpStr);
            if (tmpStr != null) while (tmpStr.indexOf("#") != -1) {
                debug("nextLine, skipping: tmpStr : " + tmpStr);
                tmpStr = d.readLine();
            }
            return tmpStr;
        } catch (IOException e) {
            System.out.println("nextLine: error reading file " + e);
        }
        return null;
    }
}
