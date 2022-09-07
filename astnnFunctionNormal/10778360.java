class BackupThread extends Thread {
    private PrintWriter getWriter(String fname) {
        f = new File(THISLOC + fname);
        if (warn_overwrite && f.exists()) System.out.printf("Warning: file %s already exists; overwriting.\n", fname);
        try {
            return new PrintWriter(new BufferedWriter(new FileWriter(f)));
        } catch (IOException ex) {
            System.out.printf("Error: Could not create file %s\n", fname);
            ex.printStackTrace();
            return null;
        }
    }
}
