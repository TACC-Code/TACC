class BackupThread extends Thread {
    private void addProfileToStore() throws Exception {
        URL url = ProfileParser.class.getClassLoader().getResource("ca/uhn/hl7v2/conf/parser/tests/example_ack.xml");
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer profile = new StringBuffer();
        int i;
        while ((i = in.read()) != -1) {
            profile.append((char) i);
        }
        in.close();
        ProfileStoreFactory.getProfileStore().persistProfile("TestProfile", profile.toString());
    }
}
