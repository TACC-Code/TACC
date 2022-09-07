class BackupThread extends Thread {
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
}
