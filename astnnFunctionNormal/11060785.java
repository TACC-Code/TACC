class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == plot) {
            loc.set(LayoutCubby.PlotMode);
            rbCascade.setEnabled(true);
            rbManual.setEnabled(true);
            return;
        } else if (e.getSource() == table) {
            loc.set(LayoutCubby.TableMode);
            rbCascade.setEnabled(false);
            rbManual.setEnabled(false);
            rbTile.setSelected(true);
            return;
        } else if (e.getSource() == rbCascade) {
            pc.setAuto(false);
            pc.cascade();
            return;
        } else if (e.getSource() == rbTile) {
            pc.setAuto(true);
            pc.tile(false);
            return;
        } else if (e.getSource() == rbManual) {
            pc.setAuto(false);
            return;
        }
        if (e.getSource() instanceof JButton) {
            setCursor(wait);
            if (frame == null) createFrame();
        } else if (e.getSource() instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) e.getSource();
            String arg = mi.getText();
            if (arg.equals("Exit")) {
                destroy();
                quitApp();
            } else if (arg.equals("Save Config")) {
                Properties config = new Properties();
                pc.getConfig(config);
                loc.set(LayoutCubby.SaveConfig);
                cc.setHash(config);
                cc.getChannel();
                config = (Properties) cc.getHash();
                try {
                    JFileChooser chooser = new JFileChooser(".");
                    chooser.setSelectedFile(new File("rbnbPlotConfig"));
                    chooser.setDialogTitle("Save Configuration");
                    int returnVal = chooser.showSaveDialog(frame);
                    if (returnVal != JFileChooser.APPROVE_OPTION) {
                        throw new Exception("File not selected");
                    }
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    System.err.println("Save config to file " + fileName);
                    FileOutputStream fos = new FileOutputStream(fileName);
                    config.store(fos, "rbnbPlot Configuration File");
                    fos.close();
                } catch (Exception fe) {
                    System.err.println("Exception, configuration not saved.");
                    fe.printStackTrace();
                }
            } else if (arg.equals("Load Config")) {
                Properties config = new Properties();
                try {
                    JFileChooser chooser = new JFileChooser(".");
                    chooser.setSelectedFile(new File("rbnbPlotConfig"));
                    chooser.setDialogTitle("Load Configuration");
                    int returnVal = chooser.showOpenDialog(frame);
                    if (returnVal != JFileChooser.APPROVE_OPTION) {
                        throw new Exception("File not selected");
                    }
                    File loadFile = chooser.getSelectedFile();
                    if (!loadFile.exists()) {
                        throw new Exception("Specified config file does not exist.");
                    }
                    String fileName = loadFile.getAbsolutePath();
                    System.err.println("Load config from file " + fileName);
                    FileInputStream fis = new FileInputStream(fileName);
                    config.load(fis);
                } catch (Exception fe) {
                    System.err.println("Exception, configuration not loaded.");
                    fe.printStackTrace();
                    return;
                }
                loc.set(LayoutCubby.LoadConfig);
                cc.setHash(config);
                cc.getChannel();
                if (config == null) {
                    String[] aboutInfo = new String[2];
                    aboutInfo[0] = new String("Error reading configuration file.");
                    aboutInfo[1] = new String("Load aborted.");
                    JInfoDialog id = new JInfoDialog(frame, true, "Error", aboutInfo);
                    id.show();
                    id.dispose();
                } else {
                    if (config.containsKey("mode") && Integer.parseInt((String) config.get("mode")) == LayoutCubby.PlotMode) {
                        plot.setSelected(true);
                    } else {
                        table.setSelected(true);
                    }
                    pc.setConfig(config);
                    pc.setDisplayMode(Integer.parseInt((String) config.get("mode")));
                    pc.setDisplayGroup(Integer.parseInt((String) config.get("dg.current")));
                }
            } else if (arg.equals("Export to Clipboard")) {
                loc.set(LayoutCubby.ExportToCB);
            } else if (arg.equals("Export to DataTurbine")) {
                loc.set(LayoutCubby.ExportToDT);
            } else if (arg.equals("Export to Matlab")) {
                loc.set(LayoutCubby.ExportToMatlab);
            } else if (arg.equals("Print")) {
                printScreen();
            } else if (arg.equals("Open RBNB")) {
                HostAndPortDialog hapd = new HostAndPortDialog(frame, true, "RBNB", "Specify RBNB Connection", environment.HOST, environment.PORT, applicationRun);
                hapd.show();
                if (hapd.state == HostAndPortDialog.OK) {
                    environment.HOST = new String(hapd.machine);
                    environment.PORT = hapd.port;
                    loc.set(LayoutCubby.OpenRBNB);
                    frame.setCursor(wait);
                    frame.setTitle("rbnbPlot by Creare " + Environment.VERSION + " (connecting to " + environment.HOST + ":" + environment.PORT + "...)");
                    if (runner == null || !runner.isAlive()) {
                        runner = new Thread(this);
                        runner.start();
                    }
                }
                hapd.dispose();
            } else if (arg.equals("Refresh")) {
                loc.set(LayoutCubby.RefreshRBNB);
            } else if (arg.equals("Close RBNB")) {
                loc.set(LayoutCubby.CloseRBNB);
                frame.setTitle("rbnbPlot by Creare (no connection)");
            } else if (arg.equals("About")) {
                System.err.println("rbnbPlot by Creare, version " + Environment.VERSION);
                String[] aboutInfo = new String[3];
                aboutInfo[0] = new String("rbnbPlot by Creare, Development Version");
                aboutInfo[1] = new String("Copyright 1998-2005 Creare, Inc.");
                aboutInfo[2] = new String("All Rights Reserved");
                JInfoDialog id = new JInfoDialog(frame, true, "About", aboutInfo);
            } else if (arg.equals("OnLine Documentation")) {
                if (applicationRun) {
                    Runtime rt = Runtime.getRuntime();
                    try {
                        Process p = rt.exec("C:\\u\\SDP\\Product\\RBNB\\V1.0\\browser.bat http://outlet.creare.com/rbnb");
                    } catch (IOException ioe) {
                        System.err.println("cannot create process!");
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }
}
