class BackupThread extends Thread {
    private void editPreferences() {
        ArrayList<TabProvider> providers = new ArrayList<TabProvider>();
        for (Plugin plugin : plugins) {
            TabProvider configEditor = plugin.getConfigEditor();
            if (configEditor != null) {
                providers.add(configEditor);
            }
        }
        providers.add(new AbstractTabProvider() {

            private Button useHttp;

            private Text port;

            private Text user;

            private Text password;

            private Button setPassword;

            @Override
            public void save() {
                boolean httpEnabled = useHttp.getSelection();
                http.setUseHttp(httpEnabled);
                if (httpEnabled) {
                    http.setPort(Integer.parseInt(port.getText()));
                    http.setUser(user.getText());
                    if (setPassword.getSelection()) {
                        String newPassword = password.getText();
                        if (newPassword.length() > 0) {
                            http.setPasswordHash(MD5.digest(newPassword));
                        } else {
                            http.setPasswordHash("");
                        }
                    }
                }
            }

            @Override
            public void load() {
                useHttp.setSelection(http.getUseHttp());
                port.setText(Integer.toString(http.getPort()));
                user.setText(http.getUser());
                setPassword.setSelection(false);
                loadPassword();
                updateEnableSelection();
                updateSetPasswordSelection();
            }

            private void loadPassword() {
                String passwordHash = http.getPasswordHash();
                password.setText(passwordHash);
            }

            @Override
            protected String getTabName() {
                return "Http";
            }

            @Override
            protected void createTabContent(Style style, Composite tab) {
                style.container(tab);
                useHttp = new Button(tab, SWT.CHECK);
                useHttp.setText("Enable HTTP");
                useHttp.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        updateEnableSelection();
                    }
                });
                label(tab, "Port");
                port = new Text(tab, SWT.BORDER);
                style.input(port);
                label(tab, "User");
                user = new Text(tab, SWT.BORDER);
                style.input(user);
                setPassword = new Button(tab, SWT.CHECK);
                setPassword.setText("Change Password");
                setPassword.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        updateSetPasswordSelection();
                    }
                });
                style.input(setPassword);
                label(tab, "Password");
                password = new Text(tab, SWT.BORDER | SWT.PASSWORD);
                password.setEnabled(false);
                style.input(password);
            }

            private void updateEnableSelection() {
                boolean enableHttp = useHttp.getSelection();
                port.setEnabled(enableHttp);
                user.setEnabled(enableHttp);
                setPassword.setEnabled(enableHttp);
            }

            private void updateSetPasswordSelection() {
                boolean shouldSetPassword = setPassword.getSelection();
                password.setEnabled(shouldSetPassword);
                if (shouldSetPassword) {
                    password.setText("");
                } else {
                    loadPassword();
                }
            }

            private void label(Composite parent, String text) {
                Label portLabel = new Label(parent, SWT.NONE);
                portLabel.setText(text);
            }
        });
        PreferencesDialog.editConfig(shell.getDisplay(), new DialogConfigImpl(layout.getPreferencesBounds()), dbConfig, trac.getConfig(), ui, new Runnable() {

            public void run() {
                handleNewConfig();
            }
        }, providers.toArray(new TabProvider[providers.size()]));
    }
}
