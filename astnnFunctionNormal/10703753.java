class BackupThread extends Thread {
    public static void SubmitPicture(String exsistingFileName, String ScriptSource) {
        if (Prefs.current.verbose) System.out.println("uploading :" + exsistingFileName);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String urlString = Prefs.current.baseURL + "upload.php";
        try {
            File file = new File(exsistingFileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"bild\";" + " filename=\"" + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            System.out.println("Fehler beim Verbindungsaufbau mit Script-Dateien.");
        } catch (IOException ioe) {
            System.out.println("Fehler beim Laden des Bildes.");
        }
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;
            String output = "";
            while ((str = inStream.readUTF()) != null) {
                output = output + str;
            }
            if (output != "") {
            }
            inStream.close();
            new File(exsistingFileName).delete();
        } catch (IOException ioex) {
            System.out.println("Fehler beim Empfangen der Serverantwort.");
        }
    }
}
