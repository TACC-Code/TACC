class BackupThread extends Thread {
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
}
