class BackupThread extends Thread {
    public void saveEMailAttach(String attachID, String realName, String fileName, String pathToSave) {
        String FileName;
        InputStream is = null;
        OutputStream fos = null;
        ResultSet rs = null;
        try {
            ebiModule.ebiPGFactory.getIEBIDatabase().setAutoCommit(true);
            PreparedStatement ps = ebiModule.ebiPGFactory.getIEBIDatabase().initPreparedStatement("SELECT * FROM MAIL_ATTACH WHERE FILENAME=? and MAIL_ATTACHID=?");
            ps.setString(1, realName);
            ps.setString(2, attachID);
            rs = ebiModule.ebiPGFactory.getIEBIDatabase().executePreparedQuery(ps);
            while (rs.next()) {
                is = rs.getBinaryStream("FILEBIN");
                FileName = pathToSave;
                fos = new FileOutputStream(FileName);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                    fos.write(buffer, 0, read);
                }
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBIPGFactory.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (FileNotFoundException ex) {
            EBIExceptionDialog.getInstance(EBIPGFactory.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (IOException ex) {
            EBIExceptionDialog.getInstance(EBIPGFactory.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            ebiModule.ebiPGFactory.getIEBIDatabase().setAutoCommit(false);
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
