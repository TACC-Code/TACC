class BackupThread extends Thread {
    private Hashtable loadDBFromServer() {
        try {
            URL url = new URL(Main.GAME_SERVER_URL + LOAD_DB_SCRIPT + "?password=" + Main.instance().getSecuritySubsystem().getPassword() + "&serial=" + Main.instance().getClientSerNumber());
            URLConnection con = url.openConnection();
            String sdata = IOUtils.toString(new GZIPInputStream(con.getInputStream()), "ASCII");
            if (StringUtils.isBlank(sdata)) return null;
            System.out.println(sdata.length());
            sdata = sdata.replace('-', '+');
            sdata = sdata.replace('_', '/');
            sdata = sdata.replace('.', '=');
            byte[] buf = Base64.decodeBase64(sdata.getBytes("ASCII"));
            ObjectInputStream oin = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(buf)));
            Hashtable data = (Hashtable) oin.readObject();
            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
