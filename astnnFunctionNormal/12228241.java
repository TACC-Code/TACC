class BackupThread extends Thread {
    private boolean extract() {
        try {
            String uploadDir = path + title;
            java.io.File theUploadDir = new java.io.File(uploadDir);
            if (!theUploadDir.isDirectory()) {
                theUploadDir.mkdirs();
            }
            String courseTitle = title;
            String zipFile = uploadDir + ".zip";
            String controlType = "choice";
            ZipFile archive = new ZipFile(zipFile);
            byte[] buffer = new byte[16384];
            for (Enumeration e = archive.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                if (!entry.isDirectory()) {
                    String filename = entry.getName();
                    filename = filename.replace('/', java.io.File.separatorChar);
                    filename = path + "CourseImports/" + courseId + "/" + filename;
                    destFile = new java.io.File(filename);
                    String parent = destFile.getParent();
                    if (parent != null) {
                        java.io.File parentFile = new java.io.File(parent);
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                    }
                    InputStream in = archive.getInputStream(entry);
                    OutputStream outStream = new FileOutputStream(filename);
                    int count;
                    while ((count = in.read(buffer)) != -1) outStream.write(buffer, 0, count);
                    in.close();
                    outStream.close();
                }
            }
            System.out.println("Try to get files \n vv");
            boolean wasdeleted = false;
            java.io.File uploadFiles[] = theUploadDir.listFiles();
            for (int i = 0; i < uploadFiles.length; i++) {
                System.out.println("Try to get files \n" + uploadFiles[i].getName());
                uploadFiles[i].delete();
            }
            System.out.println("Try to delete dir");
            System.out.println(theUploadDir.delete());
            return true;
        } catch (Exception e) {
            System.out.println("Exception caught in CourseImport");
            e.printStackTrace();
            return false;
        } finally {
        }
    }
}
