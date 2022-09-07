class BackupThread extends Thread {
    public boolean waitForAnswers() {
        try {
            Scanner scan = null;
            for (int i = 0; i < robotsCount; i++) {
                if (btRecieved[i] == null) btRecieved[i] = btconn.elementAt(i).fromBt.readLine();
                if (btRecieved[i].isEmpty()) {
                    btRecieved[i] = btconn.elementAt(i).fromBt.readLine();
                }
                if (btRecieved[i].contains("BlueCove")) {
                    btRecieved[i] = btconn.elementAt(i).fromBt.readLine();
                }
                if (btRecieved[i].isEmpty()) return false; else {
                    scan = new Scanner(btRecieved[i]);
                    if (scan.hasNextInt() && scan.nextInt() == 1) continue; else return false;
                }
            }
            for (int i = 0; i < robotsCount; i++) {
                btRecieved[i] = new String();
            }
            return true;
        } catch (Exception ex) {
            RemoteLRControlApp.writeToErr("Something was wrong in readFromBT()." + ex.toString());
            Logger.getLogger(RemoteLRControlApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
