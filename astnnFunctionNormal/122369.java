class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java GenerateFromTemplate [-debug] <input> <output> {<var>=<value>}");
            return;
        }
        int argc = args.length;
        if (args[0].equals("-debug")) {
            DEBUG = true;
            argc--;
            for (int i = 0; i < argc; i++) args[i] = args[i + 1];
        }
        int limit = argc;
        argc = 2;
        for (int i = 2; i < limit; i++) {
            if (args[i].indexOf("=") < 0) args[argc - 1] = args[argc - 1] + " " + args[i]; else {
                args[argc++] = args[i];
            }
        }
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        if (DEBUG) System.out.println("in:" + args[0] + "\nout:" + args[1]);
        try {
            if (args[0].indexOf(File.separator) != -1) inDir = args[0].substring(0, args[0].lastIndexOf(File.separator) + 1); else inDir = "";
            inStream = new FileInputStream(args[0]);
            outStream = new FileOutputStream(args[1]);
            String[] vars = new String[argc - 2];
            String[] vals = new String[argc - 2];
            for (int i = 2; i < argc; i++) {
                String arg = args[i];
                int pos = arg.indexOf("=");
                vars[i - 2] = arg.substring(0, pos);
                vals[i - 2] = arg.substring(pos + 1);
                if (DEBUG) System.out.println(vars[i - 2] + " = " + vals[i - 2]);
            }
            GenerateFromTemplate gft = new GenerateFromTemplate(inStream, outStream);
            gft.setSubst(vars, vals);
            gft.generateOutputFromTemplate();
        } finally {
            try {
                inStream.close();
                outStream.close();
            } catch (Exception e) {
            }
        }
    }
}
