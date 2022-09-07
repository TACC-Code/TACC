class BackupThread extends Thread {
    public String[] getItemIndex(Component c, Element e, String[] index) {
        int indice = Integer.parseInt(index[0]);
        String[] out = new String[4];
        for (int i = 0; i < 4; i++) {
            out[i] = null;
        }
        Element parent = e;
        String childName = null;
        String childRole = null;
        String[] children = null;
        AccessibleContext menu = c.getAccessibleContext();
        Accessible child = null;
        int count = menu.getAccessibleChildrenCount();
        if (count != 0) {
            children = new String[(2 * count)];
            for (int i = 0; i < (2 * count); i++) {
                children[i] = null;
            }
        }
        if (children != null) {
            int j = 0;
            for (int i = 0; i < count; i++) {
                child = menu.getAccessibleChild(i);
                childName = child.getAccessibleContext().getAccessibleName();
                childRole = child.getAccessibleContext().getAccessibleRole().toDisplayString(Locale.UK);
                if (stringToRole(childRole) != null) {
                    children[j] = childName;
                    children[j + 1] = childRole;
                    j = j + 2;
                }
            }
            out[0] = children[indice];
            out[1] = children[indice + 1];
            out[2] = parent.getAttributeValue("accessibleName");
            out[3] = parent.getAttributeValue("accessibleRole");
        } else {
            out = null;
        }
        return out;
    }
}
