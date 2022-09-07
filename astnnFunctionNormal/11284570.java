class BackupThread extends Thread {
    public QueryPanel(ChannelSearchPanel searchPanel) {
        _searchPanel = searchPanel;
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        add(new JLabel("Min Users", SwingConstants.RIGHT), new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        add(_minField, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        if (_searchPanel.getChannelSearch().getMinUsers() != Integer.MIN_VALUE) {
            _minField.setText(Integer.toString(_searchPanel.getChannelSearch().getMinUsers()));
        }
        add(new JLabel("Max Users", SwingConstants.RIGHT), new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        add(_maxField, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        if (_searchPanel.getChannelSearch().getMaxUsers() != Integer.MAX_VALUE) {
            _maxField.setText(Integer.toString(_searchPanel.getChannelSearch().getMaxUsers()));
        }
        JButton searchButton = new JButton("Search");
        add(searchButton, new GridBagConstraints2(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        searchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int ret = JOptionPane.showConfirmDialog(ChatApp.getChatApp(), "Some servers do not support channel searches and may \n" + "disconnect. Are you sure you want to run this search?");
                if (ret == JOptionPane.YES_OPTION || ret == JOptionPane.OK_OPTION) {
                    int min = Integer.MIN_VALUE;
                    int max = Integer.MAX_VALUE;
                    try {
                        min = Integer.parseInt(_minField.getText()) - 1;
                    } catch (Exception e) {
                    }
                    try {
                        max = Integer.parseInt(_maxField.getText()) + 1;
                    } catch (Exception e) {
                    }
                    _searchPanel.getChannelSearch().setMinUsers(min);
                    _searchPanel.getChannelSearch().setMaxUsers(max);
                    _searchPanel.getChannelSearch().start();
                }
            }
        });
    }
}
