class BackupThread extends Thread {
            public void execute(PrintWriter writer) {
                writer.println("public void " + getCheckOwnInvariantMethodName() + "(Throwable t) {");
                writer.println("  if (!" + ClassInstrumenter.SUPERCLASS_CONSTRUCTOR_COMPLETE_FIELD_NAME + " || " + ThreadState.class.getName() + "#isInFlow(this)) {");
                writer.println("    return;");
                writer.println("  }");
                writer.println("  " + ThreadState.class.getName() + ".beginInvariantCheck();");
                String qualifiedSuperclassCheckInvariantMethodFieldName = toInstrument.getName() + "#" + ClassInstrumenter.SUPERCLASS_CHECK_INVARIANT_METHOD_FIELD_NAME;
                writer.println("  if (" + qualifiedSuperclassCheckInvariantMethodFieldName + " != null) {");
                writer.println("    " + qualifiedSuperclassCheckInvariantMethodFieldName + ".invoke(this, com.stateofflow.invariantj.Utils#NULL_OBJECT_ARRAY);");
                writer.println("  }");
                writeInvariantChecks(invariantMethods, writer);
                writer.println("  " + ThreadState.class.getName() + ".endInvariantCheck(t);");
                writer.println("}");
            }
}
