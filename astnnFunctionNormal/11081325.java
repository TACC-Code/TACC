class BackupThread extends Thread {
    private void createAccessPanel() {
        accessOptions = new TN5250jMultiSelectList();
        if (props.getProperty("emul.accessDigest") != null) accessOptions.setEnabled(false);
        List<String> options = OptionAccessFactory.getInstance().getOptions();
        Hashtable<String, String> ht = new Hashtable<String, String>(options.size());
        for (int x = 0; x < options.size(); x++) {
            ht.put(LangTool.getString("key." + options.get(x)), options.get(x));
        }
        List<String> descriptions = OptionAccessFactory.getInstance().getOptionDescriptions();
        accessOptions.setListData(descriptions.toArray());
        int num = OptionAccessFactory.getInstance().getNumberOfRestrictedOptions();
        int[] si = new int[num];
        int i = 0;
        for (int x = 0; x < descriptions.size(); x++) {
            if (!OptionAccessFactory.getInstance().isValidOption(ht.get(descriptions.get(x)))) si[i++] = x;
        }
        accessOptions.setSelectedIndices(si);
        accessOptions.setSourceHeader(LangTool.getString("ss.labelActive"), JLabel.CENTER);
        accessOptions.setSelectionHeader(LangTool.getString("ss.labelRestricted"), JLabel.CENTER);
        accessPanel.setLayout(new BorderLayout());
        accessPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));
        accessPanel.add(accessOptions, BorderLayout.CENTER);
        JPanel passPanel = new JPanel();
        passPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        passPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        Action action = new AbstractAction(LangTool.getString("ss.labelSetPass")) {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (password.getPassword().length > 0) {
                    try {
                        DESSHA1 sha = new DESSHA1();
                        props.setProperty("emul.accessDigest", sha.digest(new String(password.getPassword()), "tn5205j"));
                    } catch (Exception ex) {
                    }
                }
            }
        };
        setPassButton = new JButton(action);
        if (props.getProperty("emul.accessDigest") != null) setPassButton.setEnabled(false);
        passPanel.add(setPassButton);
        password = new JPasswordField(15);
        password.setDocument(new CheckPasswordDocument());
        passPanel.add(password);
        accessPanel.add(passPanel, BorderLayout.NORTH);
    }
}
