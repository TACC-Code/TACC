class BackupThread extends Thread {
    static void testModifDate() {
        try {
            URL available_url = new URL(OOO_DICTS + "available.lst");
            URLConnection connect = available_url.openConnection();
            connect.connect();
            if (connect.getLastModified() == 0) {
                System.out.println("No date defined");
            } else {
                Date modifdate = new Date(connect.getLastModified());
                System.out.println("Modif date :" + DateFormat.getInstance().format(modifdate));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
