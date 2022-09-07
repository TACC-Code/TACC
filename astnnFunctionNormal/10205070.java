class BackupThread extends Thread {
    public OCamlScriptEngine() throws RuntimeException {
        this.context = new SimpleScriptContext();
        this.libraryPaths = new LinkedList<String>();
        this.libraryPaths.add("+cadmium");
        this.libraries = new LinkedList<String>();
        this.in = new RedirectedInputStream(System.in);
        this.out = new RedirectedOutputStream(System.out);
        this.err = new RedirectedOutputStream(System.err);
        this.printOut = new PrintStream(this.out);
        this.printErr = new PrintStream(this.err);
        try {
            final int SIZE = 1024;
            final byte[] buffer = new byte[SIZE];
            final File tmp = File.createTempFile("ocamlscript", ".toplevel");
            final InputStream is = OCamlScriptEngine.class.getResourceAsStream("script");
            final OutputStream os = new FileOutputStream(tmp);
            int read = is.read(buffer);
            while (read != -1) {
                os.write(buffer, 0, read);
                read = is.read(buffer);
            }
            is.close();
            os.close();
            final RandomAccessInputStream bytecode = new RandomAccessInputStream(tmp);
            final ByteCodeParameters params = new ByteCodeParameters(new String[] { "-w", "a" }, false, false, this.in, this.printOut, this.printErr, false, false, true, "Unix", false, tmp.getAbsolutePath(), true, null, false, false, false, false, 64 * 1024, 64 * 1024, new String[0], true);
            this.interpreter = new Interpreter(params, new File("."), bytecode);
            bytecode.close();
            this.interpreter.execute();
        } catch (final IOException ioe) {
            throw new RuntimeException("Unable to create script engine", ioe);
        } catch (final Fatal.Exception fe) {
            throw new RuntimeException("Unable to create script engine", fe);
        } catch (final CadmiumException ce) {
            throw new RuntimeException("Unable to create script engine", ce);
        }
    }
}
