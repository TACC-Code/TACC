class BackupThread extends Thread {
    public void substitute(Reader reader, Writer writer, String type) throws IllegalArgumentException, IOException {
        int t = getTypeConstant(type);
        char variable_start = (t == TYPE_SHELL ? '%' : '$');
        int c = reader.read();
        while (true) {
            while (c != -1 && c != variable_start) {
                writer.write(c);
                c = reader.read();
            }
            if (c == -1) return;
            boolean braces = false;
            c = reader.read();
            if (c == '{') {
                braces = true;
                c = reader.read();
            } else if (c == -1) {
                writer.write(variable_start);
                return;
            }
            StringBuffer nameBuffer = new StringBuffer();
            while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (((c >= '0' && c <= '9') || c == '_') && nameBuffer.length() > 0)) {
                nameBuffer.append((char) c);
                c = reader.read();
            }
            String name = nameBuffer.toString();
            boolean found = ((!braces || c == '}') && name.length() > 0 && environment.getVariable(name) != null);
            if (found) {
                writer.write(escapeSpecialChars(environment.getVariable(name), t));
                if (braces) c = reader.read();
            } else {
                writer.write(variable_start);
                if (braces) writer.write('{');
                writer.write(name);
            }
        }
    }
}
