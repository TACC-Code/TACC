class BackupThread extends Thread {
    public static void testFetchUnzip() {
        File destdir = new File("temp-jeddit");
        if (!destdir.exists()) destdir.mkdir();
        if (!destdir.isDirectory()) {
            try {
                System.out.println("File " + destdir.getCanonicalPath() + " exists");
            } catch (IOException ieo) {
            }
            return;
        } else {
            try {
                System.out.println("Downloading to " + destdir.getCanonicalPath());
            } catch (IOException ieo) {
            }
        }
        String dictArchname = "fr_FR-1990_1-3-2.zip";
        assert (dictArchname.endsWith(".zip"));
        String dir = dictArchname.substring(0, dictArchname.length() - 4);
        File extractdir = new File(destdir, dir);
        if (!extractdir.exists()) extractdir.mkdir();
        if (!extractdir.isDirectory()) {
            try {
                System.out.println("File " + extractdir.getCanonicalPath() + " exists");
            } catch (IOException ieo) {
            }
            return;
        } else {
            try {
                System.out.println("Expanding to " + extractdir.getCanonicalPath());
            } catch (IOException ieo) {
            }
        }
        File f = null;
        try {
            f = File.createTempFile(dictArchname, "tmp");
        } catch (IOException ioe) {
            System.out.println("Unable to create temp file :" + ioe);
            return;
        }
        try {
            URL available_url = new URL(OOO_DICTS + dictArchname);
            URLConnection connect = available_url.openConnection();
            connect.connect();
            InputStream is = connect.getInputStream();
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        ZipFile zip = null;
        try {
            zip = new ZipFile(f);
        } catch (ZipException ze) {
            System.out.println("invalid archive :" + ze);
            return;
        } catch (IOException ioe) {
            System.out.println("unable to read archive :" + ioe);
            return;
        }
        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry ze = entries.nextElement();
            System.out.println("Entry : " + ze);
            assert (!ze.isDirectory());
            String fn = ze.getName();
            File outFile = new File(extractdir, fn);
            if (outFile.exists()) {
                if (!outFile.delete()) {
                    System.out.println("unable to delete " + outFile);
                    return;
                }
            }
            try {
                if (!outFile.createNewFile()) {
                    System.out.println("unable to create " + outFile);
                    return;
                }
            } catch (IOException ioe) {
                System.out.println("unable to create " + outFile);
                return;
            }
            OutputStream os = null;
            try {
                os = new FileOutputStream(outFile);
            } catch (FileNotFoundException fnfe) {
                System.out.println("File " + outFile + " has disapeared...");
                return;
            }
            InputStream is = null;
            try {
                is = zip.getInputStream(ze);
            } catch (IOException ioe) {
                System.out.println("unable to read zip entry : " + ze.getName());
                return;
            }
            boolean copied = false;
            try {
                copied = IOUtilities.copyStream(new ProgressObserver() {

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
            } catch (IOException ioe) {
                System.out.println("unable to expand zip entry " + ze.getName());
                return;
            }
            if (!copied) {
                System.out.println("unable to expand zip entry " + ze.getName());
                return;
            }
            IOUtilities.closeQuietly(is);
            IOUtilities.closeQuietly(os);
        }
    }
}
