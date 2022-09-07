class BackupThread extends Thread {
    protected String createElement(HTML.Tag t, MutableAttributeSet a) {
        String tag = t.toString().toUpperCase();
        String varName = "node" + (counter++);
        String cons = constructors.get(tag);
        if (tag.equals("SCRIPT")) {
            Object value = a.getAttribute(HTML.Attribute.SRC);
            if (value != null) {
                try {
                    URL scriptSrc = new URL(input, value.toString());
                    InputStreamReader scriptReader = new InputStreamReader(scriptSrc.openConnection().getInputStream());
                    int read;
                    char[] buffer = new char[1024];
                    while ((read = scriptReader.read(buffer)) != -1) {
                        out2.write(buffer, 0, read);
                    }
                    scriptReader.close();
                } catch (IOException e) {
                    System.out.println("bad input script " + value);
                }
            } else {
                System.out.println("Entering Script");
                script = true;
            }
        }
        if (cons == null) cons = "DOMHTMLElement";
        try {
            writeElement(t, a, tag, cons, varName);
            out.write("\n");
        } catch (IOException e) {
            System.out.println("Error writing to file");
            System.exit(1);
        }
        return varName;
    }
}
