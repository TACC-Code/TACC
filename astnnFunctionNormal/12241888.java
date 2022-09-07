class BackupThread extends Thread {
        public void runTest() {
            String value = "testValue";
            String path = "comp/env";
            String name = "test";
            RuntimeContext runCtx;
            Context rootCtx;
            Context ctx;
            Context enc = null;
            InitialContext initCtx;
            VerboseStream stream = new VerboseStream();
            try {
                runCtx = RuntimeContext.newRuntimeContext();
                stream.writeVerbose("Constructing a context with comp/env/test as a test value");
                rootCtx = runCtx.getEnvContext();
                rootCtx.createSubcontext("comp");
                rootCtx.createSubcontext("comp/env");
                rootCtx.bind(path + "/" + name, value);
                ctx = (Context) rootCtx.lookup("");
                stream.writeVerbose("Test ability to read from the ENC in variety of ways");
                try {
                    if (rootCtx.lookup(path + "/" + name) != value) {
                        fail("Error: Failed to lookup name (1)");
                    }
                } catch (NameNotFoundException except) {
                    fail("Error: Failed to lookup name (2)");
                }
                try {
                    enc = (Context) rootCtx.lookup("comp");
                    enc = (Context) enc.lookup("env");
                    if (enc.lookup(name) != value) {
                        fail("Error: Failed to lookup name (3)");
                    }
                } catch (NameNotFoundException except) {
                    fail("Error: Failed to lookup name (4)");
                }
                stream.writeVerbose("Test updates on memory context reflecting in ENC");
                ctx.unbind(path + "/" + name);
                try {
                    enc.lookup(name);
                    fail("Error: NameNotFoundException not reported");
                } catch (NameNotFoundException except) {
                }
                stream.writeVerbose("Test the stack nature of the JNDI ENC");
                initCtx = new InitialContext();
                ctx.bind(path + "/" + name, value);
                RuntimeContext.setRuntimeContext(runCtx);
                RuntimeContext.setRuntimeContext(RuntimeContext.newRuntimeContext());
                try {
                    initCtx.lookup("java:" + path + "/" + name);
                    fail("Error: NotContextException not reported");
                } catch (NotContextException except) {
                }
                RuntimeContext.unsetRuntimeContext();
                try {
                    if (initCtx.lookup("java:" + path + "/" + name) != value) {
                        fail("Error: Failed to lookup name (5)");
                    }
                } catch (NamingException except) {
                    fail("Error: NamingException reported");
                }
                RuntimeContext.unsetRuntimeContext();
                try {
                    initCtx.lookup("java:" + path + "/" + name);
                    fail("Error: NamingException not reported");
                } catch (NamingException except) {
                }
                stream.writeVerbose("Test the serialization nature of JNDI ENC");
                RuntimeContext.setRuntimeContext(runCtx);
                stream.writeVerbose("Test that the JNDI ENC is read only");
                ctx.unbind(path + "/" + name);
                try {
                    initCtx.bind("java:" + name, value);
                    fail("Error: JNDI ENC not read-only (1)");
                } catch (OperationNotSupportedException except) {
                }
                ctx.bind(path + "/" + name, value);
                try {
                    initCtx.unbind("java:" + name);
                    fail("Error: JNDI ENC not read-only (2)");
                } catch (OperationNotSupportedException except) {
                }
                try {
                    ObjectOutputStream oos;
                    ObjectInputStream ois;
                    ByteArrayOutputStream aos;
                    enc = (Context) initCtx.lookup("java:" + path);
                    aos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(aos);
                    oos.writeObject(enc);
                    ois = new ObjectInputStream(new ByteArrayInputStream(aos.toByteArray()));
                    enc = (Context) ois.readObject();
                } catch (Exception except) {
                    fail("Error: Failed to (de)serialize: " + except);
                }
                RuntimeContext.unsetRuntimeContext();
                try {
                    enc.lookup(name);
                    stream.writeVerbose("Error: Managed to lookup name but java:comp not bound to thread");
                } catch (NamingException except) {
                }
                {
                }
                RuntimeContext.setRuntimeContext(runCtx);
                try {
                    if (enc.lookup(name) != value) {
                        fail("Error: Failed to lookup name (6)");
                    }
                } catch (NameNotFoundException except) {
                    fail("Error: NameNotFoundException reported");
                }
            } catch (NamingException except) {
                System.out.println(except);
                except.printStackTrace();
            }
        }
}
