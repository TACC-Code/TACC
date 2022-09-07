class BackupThread extends Thread {
    public String download() {
        File file = new File(AppInfo.getAppHome() + File.separator + "logs", getUid());
        if (file.exists()) {
            HttpServletResponse response = (HttpServletResponse) ServletActionContext.getResponse();
            response.addHeader("Content-Disposition", "attachment;filename=" + getUid());
            response.setContentType("application/octet-stream");
            try {
                FileInputStream fis = new FileInputStream(file);
                OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[16 * 1024];
                int i;
                while ((i = fis.read(buffer, 0, buffer.length)) > -1) os.write(buffer, 0, i);
                fis.close();
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return NONE;
        }
        return execute();
    }
}
