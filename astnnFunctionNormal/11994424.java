class BackupThread extends Thread {
    public void test_GetPermissions() throws Exception {
        PermissionCollection c;
        Permission per;
        Subject subject;
        CodeSource source;
        String oldProp = System.getProperty(POLICY_PROP);
        try {
            System.setProperty(POLICY_PROP, inputFile1);
            Policy p = Policy.getPolicy();
            p.refresh();
            c = p.getPermissions(null, null);
            assertFalse("Read only for empty", c.isReadOnly());
            assertFalse("Elements for empty", c.elements().hasMoreElements());
            subject = new Subject();
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new MyPrincipal("kuke"));
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new OtherPrincipal("duke"));
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new FakePrincipal("duke"));
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new MyPrincipal("duke"));
            Enumeration<Permission> e = p.getPermissions(subject, null).elements();
            per = e.nextElement();
            assertFalse("Elements: ", e.hasMoreElements());
            assertEquals("Permission: ", per, new FilePermission("/home/duke", "read, write"));
            source = new CodeSource(new URL("http://dummy.xxx"), (Certificate[]) null);
            c = p.getPermissions(subject, source);
            assertTrue("Elements: ", c.elements().hasMoreElements());
            source = new CodeSource(new URL("http://dummy.xxx"), (CodeSigner[]) null);
            c = p.getPermissions(subject, source);
            assertTrue("Elements: ", c.elements().hasMoreElements());
            source = new CodeSource(new URL("http://dummy.xxx"), (Certificate[]) null);
            subject = new Subject();
            subject.getPrincipals().add(new MyPrincipal("dummy"));
            e = p.getPermissions(subject, source).elements();
            per = e.nextElement();
            assertFalse("Elements: ", e.hasMoreElements());
            assertEquals("Permission: ", per, new RuntimePermission("createClassLoader"));
            subject = new Subject();
            c = p.getPermissions(subject, source);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new MyPrincipal("kuke"));
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new OtherPrincipal("dummy"));
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject = new Subject();
            subject.getPrincipals().add(new MyPrincipal("my"));
            c = p.getPermissions(subject, null);
            assertFalse("Elements: ", c.elements().hasMoreElements());
            subject.getPrincipals().add(new OtherPrincipal("other"));
            e = p.getPermissions(subject, null).elements();
            per = e.nextElement();
            assertFalse("Elements: ", e.hasMoreElements());
            assertEquals("Permission: ", per, new AllPermission());
            subject = new Subject();
            subject.getPrincipals().add(new MyPrincipal("bunny"));
            e = p.getPermissions(subject, null).elements();
            Permission[] get = new Permission[2];
            get[0] = e.nextElement();
            get[1] = e.nextElement();
            assertFalse("Elements: ", e.hasMoreElements());
            Permission[] set = new Permission[2];
            set[0] = new FilePermission("/home/bunny", "read, write");
            set[1] = new RuntimePermission("stopThread");
            if (get[0].equals(set[0])) {
                assertEquals("Permission: ", set[1], get[1]);
            } else {
                assertEquals("Permission: ", set[0], get[1]);
                assertEquals("Permission: ", set[1], get[0]);
            }
        } finally {
            TestUtils.setSystemProperty(POLICY_PROP, oldProp);
        }
    }
}
