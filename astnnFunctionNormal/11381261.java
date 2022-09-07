class BackupThread extends Thread {
    public static String fileUploadOne(FormFile formFile, String rootFilePath, String strAppend) {
        String strReturn = null;
        if (formFile == null) return strReturn;
        log.info("上传的文件不为空!");
        try {
            InputStream stream = formFile.getInputStream();
            if (formFile.getFileSize() > fileSize * 1024 * 10) {
                strReturn = "请把导入文件控制在20M以内！";
                return strReturn;
            }
            String contentType = formFile.getContentType();
            String filebackstr = "";
            if (contentType.equals("image/pjpeg")) filebackstr = ".jpg"; else if (contentType.equals("image/gif")) filebackstr = ".gif"; else if (contentType.equals("image/bmp")) {
                filebackstr = ".bmp";
            }
            log.info((new StringBuilder("文件类型为：：")).append(filebackstr).toString());
            File rootDir = new File(rootFilePath);
            if (!rootDir.exists() && rootDir.mkdirs()) log.info((new StringBuilder("创建上传文件根目录成功：")).append(rootFilePath).toString());
            OutputStream bos = null;
            bos = new FileOutputStream((new StringBuilder(String.valueOf(rootFilePath))).append(strAppend).toString());
            int bytesRead = 0;
            byte buffer[] = new byte[8192];
            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) bos.write(buffer, 0, bytesRead);
            bos.close();
            stream.close();
        } catch (Exception e) {
            strReturn = "上传文件出错!";
            e.printStackTrace();
            return strReturn;
        }
        return strReturn;
    }
}
