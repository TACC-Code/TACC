class BackupThread extends Thread {
    public static void createPage(String templateFileName, IFile file, String binBasePath, String srcBasePath) {
        if ("wp".equalsIgnoreCase(file.getFileExtension())) {
            try {
                IContentRenderer renderer = RendererFactory.createContentRenderer(file.getProject());
                convertWikiFile(templateFileName, file, binBasePath, srcBasePath, renderer);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (CoreException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String fname = file.getName().toLowerCase();
            if ((fname.charAt(0) == '.') || "project.index".equals(fname) || "cvs".equals(fname) || "entries".equals(fname) || "repository".equals(fname) || "root".equals(fname)) {
                return;
            }
            FileOutputStream output = null;
            InputStream contentStream = null;
            try {
                String filename = Util.getHTMLFileName(file, binBasePath, srcBasePath);
                if (filename != null) {
                    int index = filename.lastIndexOf('/');
                    if (index >= 0) {
                        File ioFile = new File(filename.substring(0, index));
                        if (!ioFile.isDirectory()) {
                            ioFile.mkdirs();
                        }
                    }
                    output = new FileOutputStream(filename);
                    contentStream = file.getContents(false);
                    int chunkSize = contentStream.available();
                    byte[] readBuffer = new byte[chunkSize];
                    int n = contentStream.read(readBuffer);
                    while (n > 0) {
                        output.write(readBuffer);
                        n = contentStream.read(readBuffer);
                    }
                }
            } catch (Exception e) {
            } finally {
                try {
                    if (output != null) output.close();
                    if (contentStream != null) contentStream.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
