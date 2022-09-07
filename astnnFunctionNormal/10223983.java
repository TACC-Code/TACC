class BackupThread extends Thread {
    @Test
    public void permission_setFromModeString_existSymbolic() throws SyntaxException, PermissionDeniedException {
        final SecurityManager mockSecurityManager = EasyMock.createMock(SecurityManager.class);
        final int ownerId = new Random().nextInt(SecurityManagerImpl.MAX_USER_ID);
        final int ownerGroupId = new Random().nextInt(SecurityManagerImpl.MAX_GROUP_ID);
        Permission permission = permission = new TestableUnixStylePermissionWithCurrentSubject(mockSecurityManager, ownerId, ownerGroupId, 0);
        permission.setMode("user=+read,+write,-execute");
        assertEquals(06, permission.getOwnerMode());
        permission = permission = new TestableUnixStylePermissionWithCurrentSubject(mockSecurityManager, ownerId, ownerGroupId, 0);
        permission.setMode("user=+execute,group=+execute,other=+execute");
        assertEquals(0111, permission.getMode());
        permission = permission = new TestableUnixStylePermissionWithCurrentSubject(mockSecurityManager, ownerId, ownerGroupId, 0666);
        permission.setMode("user=+execute,group=+execute,other=+execute");
        assertEquals(0777, permission.getMode());
        permission = permission = new TestableUnixStylePermissionWithCurrentSubject(mockSecurityManager, ownerId, ownerGroupId, 0777);
        permission.setMode("user=-read,-write,-execute,group=-read,-write,-execute,other=-read,-write,-execute");
        assertEquals(0, permission.getMode());
    }
}
