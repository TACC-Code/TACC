class BackupThread extends Thread {
    void goAhead(Request req, OutputStream os, int con_to) {
        this.req = req;
        this.os = os;
        this.con_to = con_to;
        if (os == null) bos = new ByteArrayOutputStream();
        Log.write(Log.CONN, "OutS:  Stream ready for writing");
        if (bos != null) Log.write(Log.CONN, "OutS:  Buffering all data before sending " + "request");
    }
}
