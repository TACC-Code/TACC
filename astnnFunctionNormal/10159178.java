class BackupThread extends Thread {
    public void startElement(String uri, String tag, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        wabclient.Attributes prop = new wabclient.Attributes(attributes);
        try {
            if (tag.equals("window")) {
                if (prop == null) {
                    System.err.println("window without properties");
                    return;
                }
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                Color bgcolor = prop.getValue("bgcolor", Color.white);
                String caption = prop.getValue("caption", "");
                layout = prop.getValue("layout", 0);
                boolean statusbar = prop.getValue("statusbar", false);
                frame.setTitle(caption);
                if (layout == 1) frame.getContentPane().setLayout(new FlowLayout()); else if (layout == 2) frame.getContentPane().setLayout(new BorderLayout()); else frame.getContentPane().setLayout(null);
                if (statusbar) {
                    JLabel status_bar = new JLabel();
                    status_bar.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    status_bar.setText(" ");
                    frame.org_contentpane.add(status_bar, BorderLayout.SOUTH);
                }
                frame.restorePosition(caption);
            } else if (tag.equals("menu")) {
                if (prop == null) {
                    System.err.println("menu without properties");
                    return;
                }
                String id = prop.getValue("id", "");
                String label = prop.getValue("label", "");
                if ((id != null && id.equals("WINDOW_MENU") || label.equalsIgnoreCase("window"))) {
                    windowMenu = new JMenu();
                    menu = windowMenu;
                    frame.setWindowMenu(menu);
                } else {
                    menu = new JMenu();
                }
                menu.setText(label);
                frame.getJMenuBar().add(menu);
            } else if (tag.equals("menuitem")) {
                if (prop == null) {
                    System.err.println("menuitem without properties");
                    return;
                }
                JMenuItem item;
                String action = prop.getValue("action", "");
                String label = prop.getValue("label", "");
                boolean visible = prop.getValue("visible", true);
                String icon = prop.getValue("icon", "");
                if (action.equals("WINDOW_OVERLAPPED")) {
                    item = windowMenuOverlapped = new JMenuItem();
                    item.setActionCommand("10001");
                    item.addActionListener(frame);
                } else if (action.equals("WINDOW_TILE_HORIZONTALLY")) {
                    item = windowMenuTile = new JMenuItem();
                    item.setActionCommand("10002");
                    item.addActionListener(frame);
                } else if (action.equals("WINDOW_TILE_VERTICALLY")) {
                    item = windowMenuArrange = new JMenuItem();
                    item.setActionCommand("10003");
                    item.addActionListener(frame);
                } else {
                    item = new JMenuItem();
                    item.setActionCommand(action);
                    item.addActionListener(frame);
                }
                item.setText(label);
                if (!visible) menu.setVisible(false);
                menu.add(item);
                if (icon.length() > 0) {
                    try {
                        ImageIcon img = new ImageIcon(new URL(icon));
                        BufferedImage image = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
                        Graphics g = image.createGraphics();
                        g.setColor(new Color(0, 0, 0, 0));
                        g.fillRect(0, 0, 25, 25);
                        g.drawImage(img.getImage(), 4, 4, 16, 16, (ImageObserver) null);
                        BufferedImage pressed = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
                        g = pressed.createGraphics();
                        g.setColor(new Color(0, 0, 0, 0));
                        g.fillRect(0, 0, 25, 25);
                        g.drawImage(img.getImage(), 5, 5, 16, 16, (ImageObserver) null);
                        g.setColor(new Color(132, 132, 132));
                        g.drawLine(0, 0, 24, 0);
                        g.drawLine(0, 0, 0, 24);
                        g.setColor(new Color(255, 255, 255));
                        g.drawLine(24, 24, 24, 0);
                        g.drawLine(24, 24, 0, 24);
                        BufferedImage over = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
                        g = over.createGraphics();
                        g.setColor(new Color(0, 0, 0, 0));
                        g.fillRect(0, 0, 25, 25);
                        g.drawImage(img.getImage(), 4, 4, 16, 16, (ImageObserver) null);
                        g.setColor(new Color(255, 255, 255));
                        g.drawLine(0, 0, 24, 0);
                        g.drawLine(0, 0, 0, 24);
                        g.setColor(new Color(132, 132, 132));
                        g.drawLine(24, 24, 24, 0);
                        g.drawLine(24, 24, 0, 24);
                        JButton b = new JButton(new ImageIcon(image));
                        b.setRolloverEnabled(true);
                        b.setPressedIcon(new ImageIcon(pressed));
                        b.setRolloverIcon(new ImageIcon(over));
                        b.setToolTipText(label);
                        b.setActionCommand(action);
                        b.setFocusPainted(false);
                        b.setBorderPainted(false);
                        b.setContentAreaFilled(false);
                        b.setMargin(new Insets(0, 0, 0, 0));
                        b.addActionListener(frame);
                        frame.toolbar.add(b);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (tag.equals("separator")) {
                menu.addSeparator();
            } else if (tag.equals("script")) {
                frame.beginScript();
                String url = prop.getValue("src");
                if (url.length() > 0) {
                    try {
                        BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                        String buffer;
                        while (true) {
                            buffer = r.readLine();
                            if (buffer == null) break;
                            frame.script += buffer + "\n";
                        }
                        r.close();
                        frame.endScript();
                    } catch (IOException ioe) {
                        System.err.println("[IOError] " + ioe.getMessage());
                        System.exit(0);
                    }
                }
            } else System.err.println("[frame] unparsed tag: " + tag);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
