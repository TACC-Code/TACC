class BackupThread extends Thread {
    static void threadDump(boolean useFile) {
        SimpleDateFormat fmt = new SimpleDateFormat("MM-dd_HH-mm-ss");
        OutputStreamWriter out = null;
        try {
            File file = null;
            if (useFile) {
                file = new File("ThreadDump-" + fmt.format(new Date()) + ".log");
                out = new FileWriter(file);
            } else {
                out = new OutputStreamWriter(System.out);
            }
            Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
            for (Iterator<Map.Entry<Thread, StackTraceElement[]>> iterator = traces.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Thread, StackTraceElement[]> entry = iterator.next();
                Thread thread = entry.getKey();
                out.write("Thread= " + thread.getName() + " " + (thread.isDaemon() ? "daemon" : "") + " prio=" + thread.getPriority() + "id=" + thread.getId() + " " + thread.getState());
                out.write("\n");
                StackTraceElement[] ste = entry.getValue();
                for (int i = 0; i < ste.length; i++) {
                    out.write("\t");
                    out.write(ste[i].toString());
                    out.write("\n");
                }
                out.write("---------------------------------\n");
            }
            out.close();
            out = null;
            if (useFile) {
                System.err.println("Full ThreadDump created " + file.getAbsolutePath());
            }
        } catch (IOException ignore) {
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
            }
        }
    }
}
