class BackupThread extends Thread {
    @Override
    public String getSnmpConfig() {
        return "<?xml version=\"1.0\"?>\n" + "<snmp-config " + " retry=\"3\" timeout=\"3000\"\n" + " read-community=\"public\"" + " write-community=\"private\"\n" + " port=\"161\"\n" + " version=\"v1\">\n" + "\n" + "   <definition port=\"9161\" version=\"v2c\" " + "       security-name=\"opennmsUser\" \n" + "       auth-passphrase=\"0p3nNMSv3\" \n" + "       privacy-passphrase=\"0p3nNMSv3\" >\n" + "       <specific>" + myLocalHost() + "</specific>\n" + "   </definition>\n" + "\n" + "   <definition version=\"v2c\" port=\"9161\" read-community=\"public\" proxy-host=\"" + myLocalHost() + "\">\n" + "      <specific>172.20.1.201</specific>\n" + "      <specific>172.20.1.204</specific>\n" + "   </definition>\n" + "</snmp-config>";
    }
}
