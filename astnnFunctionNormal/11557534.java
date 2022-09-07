class BackupThread extends Thread {
    @Override
    public void run() {
        InputStream input = null;
        FileOutputStream writeFile = null;
        try {
            URL url = new URL(myURL);
            URLConnection connection = url.openConnection();
            int fileLength = connection.getContentLength();
            System.out.println("Taille du fichier : " + fileLength + " ko");
            String fileType = connection.getContentType();
            System.out.println(fileType);
            if (fileLength == -1) {
                System.out.println("Invalide URL or file.");
                return;
            }
            input = connection.getInputStream();
            fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
            String targetFile = targetDirectory + '\\' + fileName;
            myProgressBar = new JProgressBar(0, fileLength);
            myProgressBar.setString(fileName);
            myProgressBar.setStringPainted(true);
            myProgressBar.setSize(494, 25);
            myMainWindows.addProgressBar(myProgressBar);
            writeFile = new FileOutputStream(targetFile);
            byte[] buffer = new byte[512];
            int read;
            int i = 0;
            while ((read = input.read(buffer)) > 0) {
                writeFile.write(buffer, 0, read);
                i = i + read;
                myProgressBar.setValue(i);
            }
            writeFile.flush();
            System.out.println("Done : " + fileName);
        } catch (IOException e) {
            System.out.println("Error while trying to download the file.");
            e.printStackTrace();
        } finally {
            try {
                writeFile.close();
                input.close();
                myProgressBar.setString("Completed : " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
