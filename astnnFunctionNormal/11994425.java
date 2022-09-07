class BackupThread extends Thread {
    public void test_Refresh() {
        Permission per;
        Subject subject;
        Enumeration<?> e;
        String oldProp = System.getProperty(POLICY_PROP);
        try {
            System.setProperty(POLICY_PROP, inputFile1);
            Policy p = Policy.getPolicy();
            p.refresh();
            subject = new Subject();
            subject.getPrincipals().add(new MyPrincipal("duke"));
            e = p.getPermissions(subject, null).elements();
            per = (Permission) e.nextElement();
            assertFalse("Elements: ", e.hasMoreElements());
            assertEquals("Permission: ", per, new FilePermission("/home/duke", "read, write"));
            System.setProperty(POLICY_PROP, inputFile2);
            p.refresh();
            e = p.getPermissions(subject, null).elements();
            per = (Permission) e.nextElement();
            assertFalse("Elements: ", e.hasMoreElements());
            assertEquals("Permission: ", per, new RuntimePermission("createClassLoader"));
        } finally {
            TestUtils.setSystemProperty(POLICY_PROP, oldProp);
        }
    }
}
