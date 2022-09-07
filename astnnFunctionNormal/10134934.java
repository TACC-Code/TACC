class BackupThread extends Thread {
    private void prepareUI() {
        registerClosingEvent();
        ArrayList<ScanSource> l = new ArrayList<ScanSource>();
        l.addAll(Configuration.getScanSources());
        pack();
        getScanSourceListBox().setModel(new DeviceBoxModel(l));
        super.getDocumentSidePanel().setPreferredSize(new java.awt.Dimension(600, 295));
        mDocumentPagesTable.setModel(mPagesmodel);
        mDocumentPagesTable.setRowHeight(100);
        mDocumentPagesTable.getColumnModel().getColumn(0).setCellRenderer(new DocumentPageTableRenderer());
        mDocumentPagesTable.setVisible(true);
        mDocumentPagesTable.setRowMargin(10);
        mDocumentPagesTable.setTableHeader(null);
        mDocumentPagesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mDocumentPagesTable.setSelectionForeground(Color.orange);
        mDocumentPagesTable.getSelectionModel().addListSelectionListener(new PageTableSelectionListener(mDocumentPagesTable, this));
        {
            mDocumentSplitter = new JSplitPane();
            super.getDocumentSidePanel().add(mDocumentSplitter, BorderLayout.CENTER);
            mDocumentSplitter.setPreferredSize(new java.awt.Dimension(600, 272));
            {
                jScrollPane1 = new JScrollPane();
                mDocumentSplitter.add(jScrollPane1, JSplitPane.LEFT);
                jScrollPane1.setViewportView(mDocumentPagesTable);
                setComponentPopupMenu(mDocumentPagesTable, getPageTablePopup());
            }
            {
                jScrollPane2 = new JScrollPane();
                mDocumentSplitter.add(jScrollPane2, JSplitPane.RIGHT);
                {
                    mDocumentPanel = new DocumentRenderer();
                    jScrollPane2.setViewportView(mDocumentPanel);
                }
            }
            mDocumentSplitter.setDividerLocation(200);
        }
        getScanAppend().setToolTipText(mActionbundle.getString("scan.append.statustext"));
        getScanAppend().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent pE) {
                ScanAction action = new ScanAction("scan.append");
                action.scan(pE, (ScanSource) getScanSourceListBox().getSelectedItem());
                ArrayList<File> pages = action.getScannedPages();
                File scandir = new File(Configuration.getTmpDir() + File.separator + "scanned");
                scandir.mkdir();
                for (File f : pages) {
                    Documentpages p = new Documentpages();
                    File dest = new File(Configuration.getTmpDir() + File.separator + "scanned" + File.separator + f.getName());
                    try {
                        FileUtils.copyFile(f.getAbsoluteFile(), dest);
                        p.setFilename(dest.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        p.setFilename(f.getAbsolutePath());
                    }
                    mPagesmodel.appendPage(p);
                }
                refreshPageNumbers();
            }
        });
        getScanBtn().setToolTipText(mActionbundle.getString("scan.append.statustext"));
        getScanBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent pE) {
                ScanAction action = new ScanAction("scan.append");
                action.scan(pE, (ScanSource) getScanSourceListBox().getSelectedItem());
                ArrayList<File> pages = action.getScannedPages();
                for (File f : pages) {
                    Documentpages p = new Documentpages();
                    p.setFilename(f.getAbsolutePath());
                    mPagesmodel.appendPage(p);
                }
                refreshPageNumbers();
            }
        });
        getScanAfterBtn().setToolTipText(mActionbundle.getString("scan.after.statustext"));
        getScanAfterBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent pE) {
                ScanAction action = new ScanAction("scan.after");
                action.scan(pE, (ScanSource) getScanSourceListBox().getSelectedItem());
                ArrayList<File> pages = action.getScannedPages();
                int selection = mDocumentPagesTable.getSelectedRow();
                for (File f : pages) {
                    Documentpages p = new Documentpages();
                    p.setFilename(f.getAbsolutePath());
                    if (selection <= 0) {
                        mPagesmodel.appendPage(p);
                    } else {
                        mPagesmodel.insertPage(selection + 1, p);
                    }
                }
                refreshPageNumbers();
            }
        });
        getScanReplaceBtn().setToolTipText(mActionbundle.getString("scan.replace.statustext"));
        getScanReplaceBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent pE) {
                ScanAction action = new ScanAction("scan.replace");
                action.scan(pE, (ScanSource) getScanSourceListBox().getSelectedItem());
                ArrayList<File> pages = action.getScannedPages();
                int selection = mDocumentPagesTable.getSelectedRow();
                for (File f : pages) {
                    Documentpages p = new Documentpages();
                    p.setFilename(f.getAbsolutePath());
                    if (selection < 0) {
                        mPagesmodel.appendPage(p);
                    } else {
                        mPagesmodel.replacePage(selection, p);
                    }
                }
                refreshPageNumbers();
                mDocumentPanel.removeAll();
            }
        });
        getScanBeforeBtn().setToolTipText(mActionbundle.getString("scan.before.statustext"));
        getScanBeforeBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent pE) {
                ScanAction action = new ScanAction("scan.before");
                action.scan(pE, (ScanSource) getScanSourceListBox().getSelectedItem());
                ArrayList<File> pages = action.getScannedPages();
                int selection = mDocumentPagesTable.getSelectedRow();
                for (File f : pages) {
                    Documentpages p = new Documentpages();
                    p.setFilename(f.getAbsolutePath());
                    if (selection < 0) {
                        selection = 0;
                    }
                    mPagesmodel.insertPage(selection, p);
                }
                refreshPageNumbers();
            }
        });
        getDeleteStackBtn().setToolTipText(mActionbundle.getString("scan.deletepage"));
        getDeleteStackBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                int selection = mDocumentPagesTable.getSelectedRow();
                mPagesToDelete.add(mPagesmodel.getPages().get(selection));
                mPagesmodel.removePage(selection);
                mDocumentPanel.removeAll();
                refreshPageNumbers();
            }
        });
        getMoveFirstBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                int selection = mDocumentPagesTable.getSelectedRow();
                if (selection != -1) {
                    mPagesmodel.moveFirst(selection);
                    refreshPageNumbers();
                    mDocumentPanel.removeAll();
                }
            }
        });
        getMoveLastBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                int selection = mDocumentPagesTable.getSelectedRow();
                if (selection != -1) {
                    mPagesmodel.moveLast(selection);
                    refreshPageNumbers();
                    mDocumentPanel.removeAll();
                }
            }
        });
        getMoveForwardBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                int selection = mDocumentPagesTable.getSelectedRow();
                if (selection != -1) {
                    mPagesmodel.moveForwards(selection);
                    refreshPageNumbers();
                    mDocumentPagesTable.getSelectionModel().setSelectionInterval(selection + 1, selection + 1);
                }
            }
        });
        getMoveBackwardsBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                int selection = mDocumentPagesTable.getSelectedRow();
                if (selection != -1) {
                    mPagesmodel.moveBackwards(selection);
                    refreshPageNumbers();
                    mDocumentPagesTable.getSelectionModel().setSelectionInterval(selection - 1, selection - 1);
                }
            }
        });
        getZoomInBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                File p;
                if (getDocumentPanel().getPage() != null) {
                    p = getDocumentPanel().getPage();
                    String ct = ContentType.getContentTypeForFilename(p.getName());
                    if (ct.startsWith("image")) {
                        ImageIcon ico = (ImageIcon) getDocumentPanel().getDisplay().getIcon();
                        BufferedImage img = (BufferedImage) ico.getImage();
                        img = Thumbnailator.createThumbnail(img, img.getWidth() + img.getWidth() / 2, img.getHeight() + img.getHeight() / 2);
                        getDocumentPanel().getDisplay().setIcon(new ImageIcon(img));
                        getDocumentPanel().updateUI();
                    }
                }
            }
        });
        getZoomOutBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                File p;
                if (getDocumentPanel().getPage() != null) {
                    p = getDocumentPanel().getPage();
                    String ct = ContentType.getContentTypeForFilename(p.getName());
                    if (ct.startsWith("image")) {
                        ImageIcon ico = (ImageIcon) getDocumentPanel().getDisplay().getIcon();
                        BufferedImage img = (BufferedImage) ico.getImage();
                        img = Thumbnailator.createThumbnail(img, img.getWidth() - img.getWidth() / 2, img.getHeight() - img.getHeight() / 2);
                        getDocumentPanel().getDisplay().setIcon(new ImageIcon(img));
                        getDocumentPanel().updateUI();
                    }
                }
            }
        });
        getRotateLeftBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                File p;
                if (getDocumentPanel().getPage() != null) {
                    p = getDocumentPanel().getPage();
                    String ct = ContentType.getContentTypeForFilename(p.getName());
                    if (ct.startsWith("image")) {
                        ImageIcon ico = (ImageIcon) getDocumentPanel().getDisplay().getIcon();
                        BufferedImage img = (BufferedImage) ico.getImage();
                        try {
                            img = Thumbnails.of(img).size(img.getWidth(), img.getHeight()).rotate(-90).asBufferedImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getDocumentPanel().getDisplay().setIcon(new ImageIcon(img));
                        getDocumentPanel().updateUI();
                    }
                }
            }
        });
        getRotateRightBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                File p;
                if (getDocumentPanel().getPage() != null) {
                    p = getDocumentPanel().getPage();
                    String ct = ContentType.getContentTypeForFilename(p.getName());
                    if (ct.startsWith("image")) {
                        ImageIcon ico = (ImageIcon) getDocumentPanel().getDisplay().getIcon();
                        BufferedImage img = (BufferedImage) ico.getImage();
                        try {
                            img = Thumbnails.of(img).size(img.getWidth(), img.getHeight()).rotate(90).asBufferedImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getDocumentPanel().getDisplay().setIcon(new ImageIcon(img));
                        getDocumentPanel().updateUI();
                    }
                }
            }
        });
        getApplyImageChangesBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                File p;
                if (getDocumentPanel().getPage() != null) {
                    p = getDocumentPanel().getPage();
                    String ct = ContentType.getContentTypeForFilename(p.getName());
                    getDocumentPanel().setPage(p);
                    if (ct.startsWith("image")) {
                        ImageIcon ico = (ImageIcon) getDocumentPanel().getDisplay().getIcon();
                        BufferedImage img = (BufferedImage) ico.getImage();
                        try {
                            Thumbnails.of(img).size(img.getWidth(), img.getHeight()).toFile(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        getUndoImageChangesBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent pE) {
                File p;
                if (getDocumentPanel().getPage() != null) {
                    p = getDocumentPanel().getPage();
                    getDocumentPanel().setPage(p);
                }
            }
        });
    }
}
