class BackupThread extends Thread {
    void genJavaDebugRead(PrintWriter writer, int depth, String readLabel, String displayValue) {
        if (!Main.genDebug) {
            return;
        }
        indent(writer, depth);
        writer.println("if (vm.traceReceives) {");
        indent(writer, depth + 1);
        writer.print("vm.printReceiveTrace(" + depth + ", \"");
        writer.print(readLabel + "(" + javaType() + "): \" + ");
        writer.println(displayValue + ");");
        indent(writer, depth);
        writer.println("}");
    }
}
