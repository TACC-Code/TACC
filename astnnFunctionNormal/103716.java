class BackupThread extends Thread {
    private void retrieveAndSave(String filename) {
        try {
            if (conn.getResponseCode() == 404) {
                System.out.println("404: " + filename);
                log404.write(filename + "\n");
                return;
            } else if (conn.getResponseCode() != 200) {
                System.out.println("HTTP code is not 404/200");
                log200.write("xx: " + filename + "\n");
                return;
            } else if (conn.getContentLength() < 30000) {
                System.out.println("Too small");
                log200.write("sm: " + filename + "\n");
                return;
            } else {
                InputStream stream = conn.getInputStream();
                FileOutputStream file = new FileOutputStream(filename);
                int c;
                while ((c = stream.read()) != -1) file.write(c);
                System.out.println("Process " + filename);
                log200.write("ok: " + filename + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }
}
