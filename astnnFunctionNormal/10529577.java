class BackupThread extends Thread {
        public void writeTest(IndentWriter pw) {
            pw.println("if (" + getAckName() + ") begin");
            pw.inc();
            pw.println("if (!" + getExistsName() + ") begin");
            pw.inc();
            pw.println("$fwrite(resultFile, \"FAIL: Illegal read from empty queue, port " + getName() + " on %d token\\n\", " + getCountName() + ");");
            pw.println("#100 $finish;");
            pw.dec();
            pw.println("end");
            pw.println("" + getCountName() + " <= " + getCountName() + " + 1;");
            pw.println("$vectorPop(" + getHandleName() + ");");
            pw.dec();
            pw.println("end else begin");
            pw.inc();
            pw.println("$vectorPeek(" + getHandleName() + ");");
            pw.dec();
            pw.println("end");
        }
}
