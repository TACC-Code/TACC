class BackupThread extends Thread {
    public static void main(String args[]) {
        args = new String[] { "ExtractMassChromatograms", "-h" };
        try {
            if (args.length < 1) {
                System.err.println("[ERROR]: the tool-name to be executed needs to be supplied.");
                System.exit(0);
            }
            Class<? extends Object> cls = null;
            for (Class<? extends Object> c : Tool.getAllClasses("mzmatch.ipeak")) {
                if (c.getName().endsWith(args[0])) {
                    cls = c;
                    break;
                }
            }
            if (cls == null) {
                System.err.println("[ERROR]: unable to locate the requested application '" + args[0] + "'");
                System.exit(0);
            }
            String real_args[] = new String[args.length - 1];
            for (int i = 0; i < args.length - 1; ++i) real_args[i] = args[i + 1];
            for (Method method : cls.getMethods()) {
                if (method.getName().equals("main")) {
                    method.invoke(null, (Object) real_args);
                    break;
                }
            }
        } catch (Exception e) {
            Tool.unexpectedError(e, "mzmatch.Execute");
        }
    }
}
