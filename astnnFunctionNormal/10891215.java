class BackupThread extends Thread {
    public static void main(String[] args) throws Throwable {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            JFrame frame = new JFrame();
            frame.setSize(650, 500);
            frame.setLocationRelativeTo(null);
            frame.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Your computer is not running Windows. To use the installer,<br/>" + "your computer needs to be running windows. However, you can still use<br/>" + "Convergia, but you will have to manually install it. for instructions<br/>" + "on how to manually install Convergia, visit http://convergia.sf.net/manualinstall.html"));
            frame.show();
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            return;
        }
        Frame0 frame0 = new Frame0();
        frame0.setDefaultCloseOperation(frame0.DO_NOTHING_ON_CLOSE);
        frame0.setLocationRelativeTo(null);
        frame0.show();
        new JFileChooser();
        frame0.dispose();
        final Frame1 frame1 = new Frame1();
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(frame1.EXIT_ON_CLOSE);
        frame1.show();
        frame1.getInstallButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (frame1.getInstallFile() == null) JOptionPane.showMessageDialog(frame1, "You must select a folder to install to first."); else if (!frame1.getInstallFile().exists() && !frame1.getInstallFile().mkdirs()) JOptionPane.showMessageDialog(frame1, "The folder you selected didn't exist, but it could not be created."); else if (!frame1.getInstallFile().canRead()) JOptionPane.showMessageDialog(frame1, "That folder can't be read. Please choose another folder."); else if (!frame1.getInstallFile().canWrite()) JOptionPane.showMessageDialog(frame1, "That folder can't be written to. Please choose another folder."); else {
                    frame1.dispose();
                    proceedFrom1 = true;
                }
            }
        });
        while (!proceedFrom1) {
            Thread.sleep(500);
        }
        Frame2 frame2 = new Frame2();
        frame2.setLocationRelativeTo(null);
        frame2.setDefaultCloseOperation(frame2.DO_NOTHING_ON_CLOSE);
        frame2.show();
        System.out.println("user chose " + frame1.getInstallFile());
        File installTo = frame1.getInstallFile();
        URL updateUrl = new URL(UPDATE_URL);
        InputStream in;
        try {
            in = updateUrl.openStream();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame2, "You are not connected to the internet. Connect " + "to the internet, and then run the installer again.");
            System.exit(0);
            return;
        }
        Properties p = new Properties();
        p.load(in);
        int versionindex = Integer.parseInt(p.getProperty("versionindex"));
        URL url = new URL(p.getProperty("url"));
        frame2.getStatusLabel().setText("Convergia Installer is downloading Convergia, please wait...");
        File jarfile = File.createTempFile("cvginstall", ".jar");
        System.out.println("jarfile is " + jarfile.getAbsolutePath());
        jarfile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(jarfile);
        byte[] buffer = new byte[4096];
        int amount;
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.connect();
        int filesize = con.getContentLength();
        int amountRead = 0;
        if (filesize != -1) {
            frame2.getProgressBar().setIndeterminate(false);
            frame2.getProgressBar().setMinimum(0);
            frame2.getProgressBar().setMaximum(filesize / 1024);
            frame2.getProgressBar().setValue(0);
        }
        in = con.getInputStream();
        while ((amount = in.read(buffer)) != -1) {
            fos.write(buffer, 0, amount);
            amountRead += amount;
            frame2.getProgressBar().setValue(amountRead / 1024);
        }
        fos.flush();
        fos.close();
        in.close();
        System.out.println("downloaded");
        frame2.getStatusLabel().setText("Convergia Installer is installing Convergia, please wait...");
        installTo.mkdirs();
        System.out.println("about to extract");
        extractUpdates(jarfile, installTo, frame2.getProgressBar());
        System.out.println("finished extracting");
        String installPath = installTo.getCanonicalPath();
        if (installPath.endsWith("/") || installPath.endsWith("\\")) installPath = installPath.substring(0, installPath.length() - 1);
        System.out.println("install path is " + installPath);
        String runCommand = readFile(new File(installTo, "windowsrunscript"));
        runCommand = runCommand.replace("%INSTALLDIR%", installPath);
        writeFile(runCommand, new File(System.getProperty("user.home"), "Desktop/Convergia.bat"));
        frame2.dispose();
        Frame3 frame3 = new Frame3();
        frame3.setLocationRelativeTo(null);
        frame3.show();
        frame3.setDefaultCloseOperation(frame3.EXIT_ON_CLOSE);
    }
}
