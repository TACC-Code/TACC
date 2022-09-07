class BackupThread extends Thread {
    private void loadUserMemberList() throws IOException {
        String svnAuthFile = SVNAdminServiceProperties.getHtpasswdUserAuthFile();
        InputStream is = null;
        try {
            final Set<String> tempMemberList = new HashSet<String>();
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(svnAuthFile);
            if (is == null) {
                is = getClass().getResourceAsStream(svnAuthFile);
                if (is == null) {
                    File authFile = new File(svnAuthFile);
                    if (!authFile.exists()) {
                        throw new RuntimeException("could not locate svn auth file: " + svnAuthFile);
                    }
                    if (!authFile.canRead() || !authFile.canWrite()) {
                        throw new RuntimeException("could not read/write svn auth file: " + svnAuthFile);
                    }
                    is = new FileInputStream(authFile);
                    if (is == null) {
                        throw new RuntimeException("could not locate svn auth file: " + svnAuthFile);
                    }
                }
            }
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            int indexOfSemiColon = -1;
            String username = null;
            String line = br.readLine();
            while (line != null) {
                indexOfSemiColon = line.indexOf(':');
                username = line.substring(0, indexOfSemiColon);
                tempMemberList.add(username);
                line = br.readLine();
            }
            this.memberList = tempMemberList;
        } finally {
            try {
                is.close();
                is = null;
            } catch (Exception ignore) {
            }
        }
    }
}
