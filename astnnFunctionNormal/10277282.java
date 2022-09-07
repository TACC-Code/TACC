class BackupThread extends Thread {
    public void registerHelpers(ComponentHelper componentHelpers) {
        componentHelpers.addComponentType(new PageHelper());
        componentHelpers.addComponentType(new LayerHelper());
        componentHelpers.addComponentType(new CheckBoxHelper());
        componentHelpers.addComponentType(new ComboBoxHelper());
        componentHelpers.addComponentType(new LabelHelper());
        componentHelpers.addComponentType(new ListHelper(true));
        componentHelpers.addComponentType(new MenuHelper());
        componentHelpers.addComponentType(new MenuItemHelper());
        componentHelpers.addComponentType(new PanelHelper());
        componentHelpers.addComponentType(new RadioButtonHelper());
        componentHelpers.addComponentType(new ScrollPaneHelper());
        componentHelpers.addComponentType(new TableHelper());
        componentHelpers.addComponentType(new TextAreaHelper());
        componentHelpers.addComponentType(new UnknownHelper());
        componentHelpers.addComponentType(new ImageButtonHelper());
        try {
            URL url = getClass().getClassLoader().getResource("org/formaria/editor/project/pages/components/properties.xml");
            Reader r = new BufferedReader(new InputStreamReader(url.openStream()));
            XmlElement regRoot = XmlSource.read(r);
            Vector registrationNodes = regRoot.getChildren();
            int numElements = registrationNodes.size();
            for (int i = 0; i < numElements; i++) {
                XmlElement regElement = (XmlElement) registrationNodes.elementAt(i);
                GenericPropertyHelper ph = new GenericPropertyHelper();
                ph.setClassName(regElement.getAttribute("class"));
                ph.setComponentType(regElement.getAttribute("name"));
                String s = regElement.getAttribute("allowsChildren");
                if ((s != null) && "true".equals(s)) ph.setAllowsChildren(true);
                s = regElement.getAttribute("usesContentFile");
                if ((s != null) && "true".equals(s)) ph.setUsesContentFile(true);
                s = regElement.getAttribute("restrictsSize");
                if ((s != null) && "true".equals(s)) ph.setRestrictsSize(true);
                Vector propNodes = regElement.getChildren();
                int numProps = propNodes.size();
                for (int j = 0; j < numProps; j++) {
                    XmlElement propElement = (XmlElement) propNodes.elementAt(j);
                    String tag = propElement.getName();
                    if (tag.equals("extension")) {
                        String[] values = propElement.getAttribute("values").split(";");
                        ph.setFileExtensions(propElement.getAttribute("desc"), propElement.getAttribute("default"), values);
                    } else {
                        String propName = propElement.getAttribute("name");
                        ph.addProperty(propName);
                        String defValue = propElement.getAttribute("default");
                        if ((defValue != null) && (defValue.length() > 0)) ph.addDefaultValues(propName, defValue);
                    }
                }
                componentHelpers.addComponentType(ph);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
