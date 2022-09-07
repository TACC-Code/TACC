class BackupThread extends Thread {
    public void extract(String s) {
        File file = new File(s);
        JFileChooser jfilechooser = new JFileChooser();
        jfilechooser.setCurrentDirectory(new File("."));
        jfilechooser.setDialogType(0);
        jfilechooser.setDialogTitle("Select destination directory for extracting " + file.getName());
        jfilechooser.setMultiSelectionEnabled(false);
        jfilechooser.setFileSelectionMode(1);
        if (jfilechooser.showDialog(this, "Select") != 0) {
            return;
        }
        File file1 = jfilechooser.getSelectedFile();
        byte abyte0[] = new byte[1024];
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MM/dd/yyyy hh:mma", Locale.getDefault());
        Object obj = null;
        boolean flag = false;
        ZipFile zipfile = null;
        FileOutputStream fileoutputstream = null;
        InputStream inputstream = null;
        try {
            zipfile = new ZipFile(file);
            int i = zipfile.size();
            int j = 0;
            ProgressMonitor progressmonitor = new ProgressMonitor(getParent(), "Extracting files...", "starting", 0, i - 4);
            progressmonitor.setMillisToDecideToPopup(0);
            progressmonitor.setMillisToPopup(0);
            Enumeration enumeration = zipfile.entries();
            for (int k = 0; k < i; k++) {
                ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
                if (zipentry.isDirectory()) {
                    continue;
                }
                String s1 = zipentry.getName();
                if (myClassName.equals(s1) || MANIFEST.equals(s1.toUpperCase())) {
                    continue;
                }
                j++;
                progressmonitor.setProgress(k);
                progressmonitor.setNote(s1);
                if (progressmonitor.isCanceled()) {
                    return;
                }
                inputstream = zipfile.getInputStream(zipentry);
                File file2 = new File(file1, s1);
                Date date;
                date = date = new Date(zipentry.getTime());
                if (!flag && file2.exists()) {
                    Object aobj[] = { "Yes", "Yes To All", "No" };
                    Date date1 = new Date(file2.lastModified());
                    Long long1 = new Long(zipentry.getSize());
                    String s2 = "File name conflict: There is already a file with that name on the disk!\n\nFile name: " + file2.getName() + "\nExisting file: " + simpledateformat.format(date1) + ",  " + file2.length() + "Bytes" + "\nFile in archive:" + simpledateformat.format(date) + ",  " + long1 + "Bytes" + "\n\nWould you like to overwrite the file?";
                    int i1 = JOptionPane.showOptionDialog(this, s2, "Warning", -1, 2, null, aobj, aobj[0]);
                    if (i1 == 2) {
                        continue;
                    }
                    if (i1 == 1) {
                        flag = true;
                    }
                }
                File file3 = new File(file2.getParent());
                if (file3 != null && !file3.exists()) {
                    file3.mkdirs();
                }
                fileoutputstream = new FileOutputStream(file2);
                do {
                    int l = inputstream.read(abyte0, 0, abyte0.length);
                    if (l <= 0) {
                        break;
                    }
                    fileoutputstream.write(abyte0, 0, l);
                } while (true);
                fileoutputstream.close();
                file2.setLastModified(date.getTime());
            }
            progressmonitor.close();
            zipfile.close();
            getToolkit().beep();
            JOptionPane.showMessageDialog(this, "Extracted " + j + " file" + (j <= 1 ? "" : "s") + " from the\n" + s + "\narchive into the\n" + file1.getPath() + "\ndirectory.", "Zip Self Extractor", 1);
        } catch (Exception exception) {
            System.out.println(exception);
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException _ex) {
                }
            }
            if (fileoutputstream != null) {
                try {
                    fileoutputstream.close();
                } catch (IOException _ex) {
                }
            }
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (IOException _ex) {
                }
            }
        }
    }
}
