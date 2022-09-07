class BackupThread extends Thread {
        public boolean hasUserAccess(boolean accessToParent, String accessType, List<String> userGroups) throws Exception {
            List<String> accessList;
            if (accessToParent) {
                if (accessType.equals("read")) accessList = denyReading; else if (accessType.equals("write")) accessList = denyWriting; else throw new Exception("Access type can be 'read' or 'write' but not '" + accessType + "'");
            } else {
                if (accessType.equals("read")) accessList = grantReading; else if (accessType.equals("write")) accessList = grantWriting; else throw new Exception("Access type can be 'read' or 'write' but not '" + accessType + "'");
            }
            Iterator<String> userGroupsIterator = userGroups.iterator();
            while (userGroupsIterator.hasNext()) {
                String userGroup = userGroupsIterator.next();
                if (accessList.contains(userGroup)) return !accessToParent;
            }
            return accessToParent;
        }
}
