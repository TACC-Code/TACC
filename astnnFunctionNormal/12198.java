class BackupThread extends Thread {
    public void setInputStream(InputStream ins) {
        int bytes_read = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data[] = new byte[1024];
        try {
            while ((bytes_read = ins.read(data)) > 0) baos.write(data, 0, bytes_read);
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_area.setText(baos.toString());
    }
}
