class BackupThread extends Thread {
    @Override
    protected void addOption(StringBuffer sb, String label, String value, boolean matched) {
        try {
            if (label.startsWith("$")) {
                String optionBundle = bundle;
                int colonIndex = label.indexOf(":");
                if (colonIndex != -1) {
                    optionBundle = label.substring(1, colonIndex);
                    label = label.substring(colonIndex);
                }
                String[] args = label.split("\\|");
                if (args.length > 1) {
                    String[] args2 = new String[args.length - 1];
                    for (int i = 0; i < args2.length; ++i) {
                        args2[i] = args[i + 1].replaceAll("\\\\\\|", "\\|");
                    }
                    label = TagUtils.getInstance().message(pageContext, optionBundle, locale, args[0].substring(1), args2);
                } else {
                    label = TagUtils.getInstance().message(pageContext, optionBundle, locale, label.substring(1));
                }
            }
        } catch (JspException x) {
        }
        if (getParent() == null || !(getParent() instanceof de.iritgo.aktera.struts.tags.html.SelectTag) || !((SelectTag) getParent()).getReadOnly()) {
            options.add(new Option(label, value, matched));
        } else if (matched) {
            try {
                TagUtils.getInstance().write(pageContext, label);
            } catch (JspException x) {
            }
        }
    }
}
