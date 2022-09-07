class BackupThread extends Thread {
    static void testFetch() {
        try {
            URL available_url = new URL(OOO_DICTS + "available.lst");
            URLConnection connect = available_url.openConnection();
            connect.connect();
            InputStream is = connect.getInputStream();
            File f = File.createTempFile("available", "lst");
            OutputStream os = new FileOutputStream(f);
            boolean copied = IOUtilities.copyStream(new ProgressObserver() {

                public void setMaximum(long v) {
                    System.out.println("max:" + v);
                }

                public void setStatus(String s) {
                    System.out.println("status:" + s);
                }

                public void setValue(long v) {
                    System.out.println("val:" + v);
                }
            }, is, os, true);
            if (!copied) {
                System.out.println("Could not copy !!");
                return;
            }
            IOUtilities.closeQuietly(os);
            String enc = connect.getContentEncoding();
            FileInputStream fis = new FileInputStream(f);
            Reader r;
            System.out.println("Encoding:" + enc);
            if (enc != null) {
                try {
                    r = new InputStreamReader(fis, enc);
                } catch (UnsupportedEncodingException uee) {
                    r = new InputStreamReader(fis, "UTF-8");
                }
            } else {
                r = new InputStreamReader(fis, "UTF-8");
            }
            BufferedReader br = new BufferedReader(r);
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                System.out.println(line);
            }
            IOUtilities.closeQuietly(fis);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
