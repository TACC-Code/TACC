class BackupThread extends Thread {
    private void doAuthWithPermission(int bits, boolean readExceptionThrown, boolean writeExceptionThrown) throws Exception {
        User u = create(User.class);
        UserStatus us = create(UserStatus.class);
        UserStatusPermission usp = create(UserStatusPermission.class);
        usp.setName(thePage.getPermissionName());
        usp.setPermissionBits(new Integer(bits));
        us.addToPermissions(usp);
        u.setStatus(us);
        CoursePermission cp = create(CoursePermission.class);
        cp.setPermissionBits(new Integer(Permission.READ));
        us.addToPermissions(cp);
        CourseAvailabilityPermission cap = create(CourseAvailabilityPermission.class);
        cap.setPermissionBits(new Integer(Permission.READ));
        us.addToPermissions(cap);
        u.setAuthentic(true);
        visit.setUser(u);
        try {
            doAuth(readExceptionThrown, false);
            doAuth(writeExceptionThrown, true);
        } catch (Error e) {
            discardChanges();
            throw e;
        }
        discardChanges();
    }
}
