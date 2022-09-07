class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                if (password.getPassword().length > 0) {
                    try {
                        DESSHA1 sha = new DESSHA1();
                        props.setProperty("emul.accessDigest", sha.digest(new String(password.getPassword()), "tn5205j"));
                    } catch (Exception ex) {
                    }
                }
            }
}
