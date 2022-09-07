class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("-help")) {
            System.out.println("Syntax is\n" + "java SetupDB <driver> <user> <password> <URL>");
        } else {
            DataSource source = new DataSource_impl();
            source.setDriver(args[0]);
            source.setUser(args[1]);
            source.setPassword(args[2]);
            source.setURL(args[3]);
            source.setCorePool(5);
            source.setKeepAlive(5000);
            source.setSleep(5000);
            PersistenceManager.init(source);
            SQLGroupsDAO gdao = new SQLGroupsDAO(PersistenceManager.getConnectionPool());
            SQLGroupTAO gtao = (SQLGroupTAO) gdao.create();
            gtao.setName("admin");
            SQLUsersDAO udao = new SQLUsersDAO(PersistenceManager.getConnectionPool());
            SQLUserTAO utao = (SQLUserTAO) udao.create();
            gtao.addUser(utao);
            utao.addToGroup(gtao);
            utao.setName("admin");
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance(ALGORITHM);
            byte[] bytes = digest.digest(new String("admin").getBytes());
            utao.setPassword(bytes);
            Connection con = PersistenceManager.getConnectionPool().borrow();
            Statement stmt = con.createStatement();
            stmt.execute("checkpoint");
            PersistenceManager.getConnectionPool().destroy();
        }
    }
}
