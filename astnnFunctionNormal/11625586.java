class BackupThread extends Thread {
    public void execUpSql() {
        String name = getClass().getCanonicalName().replace(".", "/");
        InputStream in = getClass().getResourceAsStream(name + ".sql");
        try {
            StringWriter writer = new StringWriter();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (reader.ready()) {
                writer.write(reader.readLine());
            }
            reader.close();
            Statement statement = DbManager.instance().getConnection().createStatement();
            statement.execute(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
