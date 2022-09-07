class BackupThread extends Thread {
    public void fieldToFile(ResultSet resultset, String s, String s1) throws ServletException, IOException, SmartUploadException, SQLException {
        try {
            if (m_application.getRealPath(s1) != null) s1 = m_application.getRealPath(s1);
            InputStream inputstream = resultset.getBinaryStream(s);
            FileOutputStream fileoutputstream = new FileOutputStream(s1);
            int i;
            while ((i = inputstream.read()) != -1) fileoutputstream.write(i);
            fileoutputstream.close();
        } catch (Exception exception) {
            throw new SmartUploadException("Unable to save file from the DataBase (1020).");
        }
    }
}
