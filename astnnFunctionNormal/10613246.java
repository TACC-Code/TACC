class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        try {
            FisProperties.loadProperties();
        } catch (Exception ex) {
        }
        Properties props = System.getProperties();
        String dir = props.getProperty("GUI_DEFAULTS", "none");
        if (dir.equals("none")) dir = props.getProperty("user.dir");
        JFileChooser chooser = new JFileChooser(dir);
        chooser.addChoosableFileFilter(new FisFileFilter(OLD_suff, OLD_desc));
        chooser.addChoosableFileFilter(new FisFileFilter(XML_suff, XML_desc));
        chooser.setAccessory(buildPreview());
        chooser.addPropertyChangeListener(this);
        int r = chooser.showSaveDialog(g);
        if (r == JFileChooser.APPROVE_OPTION) {
            String t = chooser.getSelectedFile().getAbsolutePath();
            boolean overwrite = true;
            if (!chooser.getFileFilter().getDescription().equals(OLD_desc)) {
                if (!t.endsWith(XML_suff)) t = t + XML_suff;
                if ((new File(t)).exists()) {
                    File file = new File(t);
                    if (file != null) {
                        Document document;
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        try {
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            builder.setErrorHandler(new XmlErrorHandler());
                            builder.setEntityResolver(new XmlEntityResolver());
                            document = builder.parse(file);
                            Element element = document.getDocumentElement();
                            String name = appName(g);
                            if (!(name.equals(element.getAttribute("name").trim()))) {
                                JOptionPane.showMessageDialog(g, "This XML file is not from " + name, "Warning", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                        }
                        document = null;
                        factory = null;
                        int ans = JOptionPane.showConfirmDialog(g, "The file already exists, overwrite?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                        if (ans == JOptionPane.YES_OPTION) {
                            overwrite = true;
                            try {
                                copyFile(t, t + "~");
                            } catch (IOException ex) {
                                int err_ans = JOptionPane.showConfirmDialog(g, "Can't create backup file " + t + "~" + "\nDo you wish to continue anyways?", "Warning", JOptionPane.YES_NO_OPTION);
                                if (err_ans != JOptionPane.YES_OPTION) {
                                    overwrite = false;
                                }
                            }
                        } else overwrite = false;
                    }
                }
                if (overwrite) {
                    if (!preview.getText().equals(defaultDescription)) {
                        g.getAppInfo().description = getComment(preview.getText());
                    }
                    XmlSerializationFactory sf = new XmlSerializationFactory(g, t);
                    try {
                        sf.saveApp();
                    } catch (Exception ex) {
                        Dialogs.ShowErrorDialog(g, "Error saving file!\n" + ex.getMessage());
                    }
                }
            } else {
                if (!t.endsWith(OLD_suff)) t = t + OLD_suff;
                if ((new File(t)).exists()) {
                    int ans = JOptionPane.showConfirmDialog(g, "The file already exists, overwrite?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) overwrite = true; else overwrite = false;
                }
                if (overwrite) {
                    SerializationFactory sf = new SerializationFactory(g.componentPanel, t);
                    try {
                        sf.save();
                    } catch (Exception ex) {
                        Dialogs.ShowErrorDialog(g, "Error saving file!");
                    }
                }
            }
        }
    }
}
