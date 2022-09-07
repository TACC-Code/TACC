class BackupThread extends Thread {
    private void writeInvariantChecks(final CtMethod[] invariantMethods, PrintWriter writer) {
        if (invariantMethods.length > 0) {
            for (int i = 0; i < invariantMethods.length; i++) {
                String invariantMethodName = invariantMethods[i].getName();
                writer.println("  try {");
                writer.println("    if (!" + invariantMethodName + "()) {");
                writer.println("      " + ThreadState.class.getName() + ".addFailedInvariant(\"" + getInvariantNameFromMethodName(invariantMethodName) + "\");");
                writer.println("    }");
                writer.println("  } catch (Exception ex) {");
                writer.println("    " + ThreadState.class.getName() + ".addExceptionInInvariantCheck(\"" + getInvariantNameFromMethodName(invariantMethodName) + "\", ex);");
                writer.println("  } catch (com.stateofflow.invariantj.InvariantViolationError ex) {");
                writer.println("    " + ThreadState.class.getName() + ".addExceptionInInvariantCheck(\"" + getInvariantNameFromMethodName(invariantMethodName) + "\", ex);");
                writer.println("  }");
            }
        }
    }
}
