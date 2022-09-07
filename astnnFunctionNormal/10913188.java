class BackupThread extends Thread {
    private void loadUserGroupAndDirectoryAccess() throws IOException {
        String svnDirAccessFile = SVNAdminServiceProperties.getUserDirectoryAccessFile();
        InputStream is = null;
        try {
            final Set<String> groupList = new HashSet<String>();
            List<String> membersForGroupList = null;
            final Map<String, List<String>> groupMemberMap = new HashMap<String, List<String>>();
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(svnDirAccessFile);
            if (is == null) {
                is = getClass().getResourceAsStream(svnDirAccessFile);
                if (is == null) {
                    File svnDirAccess = new File(svnDirAccessFile);
                    if (!svnDirAccess.exists()) {
                        throw new RuntimeException("could not locate svn directory access file: " + svnDirAccessFile);
                    }
                    if (!svnDirAccess.canRead() || !svnDirAccess.canWrite()) {
                        throw new RuntimeException("could not read/write svn directory access file: " + svnDirAccessFile);
                    }
                    is = new FileInputStream(svnDirAccess);
                    if (is == null) {
                        throw new RuntimeException("could not locate svn directory access file: " + svnDirAccessFile);
                    }
                }
            }
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            int indexOfEqual = -1;
            String groupName = null;
            boolean groupsFound = false;
            String tempLine = null;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (groupsFound == false && line.trim().equalsIgnoreCase("[groups]")) {
                    groupsFound = true;
                    continue;
                }
                if (groupsFound) {
                    indexOfEqual = line.indexOf('=');
                    if (indexOfEqual != -1) {
                        groupName = line.substring(0, indexOfEqual);
                        groupName = groupName.trim();
                        groupList.add(groupName);
                        membersForGroupList = new ArrayList<String>();
                        String membersCSV = line.substring(indexOfEqual + 1, line.length());
                        StringTokenizer st = new StringTokenizer(membersCSV.trim(), ",");
                        while (st.hasMoreTokens()) {
                            String member = st.nextToken();
                            if (!membersForGroupList.contains(member.trim())) {
                                membersForGroupList.add(member.trim());
                            }
                        }
                        LOG.debug("adding members : " + membersForGroupList + ", for groupName: " + groupName);
                        groupMemberMap.put(groupName, membersForGroupList);
                    } else if (line.trim().equals("")) {
                        continue;
                    } else if (line.indexOf('[') != -1) {
                        tempLine = line;
                        break;
                    }
                }
            }
            Map<String, List<String>> projectAccessMap = new HashMap<String, List<String>>();
            List<String> groupPathsList = new ArrayList<String>();
            String path = null;
            boolean pathFound = false;
            for (String line = tempLine; line != null; line = br.readLine()) {
                line = line.trim();
                if (line.startsWith("[/")) {
                    if (pathFound) {
                        projectAccessMap.put(path, groupPathsList);
                    }
                    groupPathsList = new ArrayList<String>();
                    path = line.substring(1, line.indexOf(']'));
                    LOG.debug("path found: " + path);
                    pathFound = true;
                    continue;
                } else if (!line.equals("")) {
                    groupPathsList.add(line);
                }
            }
            if (pathFound) {
                projectAccessMap.put(path, groupPathsList);
            }
            this.groupMembersMap = groupMemberMap;
            this.projectAccessMap = projectAccessMap;
        } finally {
            try {
                is.close();
                is = null;
            } catch (Exception ignore) {
            }
        }
    }
}
