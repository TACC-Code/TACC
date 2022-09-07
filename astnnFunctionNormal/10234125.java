class BackupThread extends Thread {
    private CharInfo(String entitiesResource, String method, boolean internal) {
        this();
        m_charToString = new HashMap();
        ResourceBundle entities = null;
        boolean noExtraEntities = true;
        if (internal) {
            try {
                entities = PropertyResourceBundle.getBundle(entitiesResource);
            } catch (Exception e) {
            }
        }
        if (entities != null) {
            Enumeration keys = entities.getKeys();
            while (keys.hasMoreElements()) {
                String name = (String) keys.nextElement();
                String value = entities.getString(name);
                int code = Integer.parseInt(value);
                boolean extra = defineEntity(name, (char) code);
                if (extra) noExtraEntities = false;
            }
        } else {
            InputStream is = null;
            try {
                if (internal) {
                    is = CharInfo.class.getResourceAsStream(entitiesResource);
                } else {
                    ClassLoader cl = ObjectFactory.findClassLoader();
                    if (cl == null) {
                        is = ClassLoader.getSystemResourceAsStream(entitiesResource);
                    } else {
                        is = cl.getResourceAsStream(entitiesResource);
                    }
                    if (is == null) {
                        try {
                            URL url = new URL(entitiesResource);
                            is = url.openStream();
                        } catch (Exception e) {
                        }
                    }
                }
                if (is == null) {
                    throw new RuntimeException(Utils.messages.createMessage(MsgKey.ER_RESOURCE_COULD_NOT_FIND, new Object[] { entitiesResource, entitiesResource }));
                }
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    reader = new BufferedReader(new InputStreamReader(is));
                }
                String line = reader.readLine();
                while (line != null) {
                    if (line.length() == 0 || line.charAt(0) == '#') {
                        line = reader.readLine();
                        continue;
                    }
                    int index = line.indexOf(' ');
                    if (index > 1) {
                        String name = line.substring(0, index);
                        ++index;
                        if (index < line.length()) {
                            String value = line.substring(index);
                            index = value.indexOf(' ');
                            if (index > 0) {
                                value = value.substring(0, index);
                            }
                            int code = Integer.parseInt(value);
                            boolean extra = defineEntity(name, (char) code);
                            if (extra) noExtraEntities = false;
                        }
                    }
                    line = reader.readLine();
                }
                is.close();
            } catch (Exception e) {
                throw new RuntimeException(Utils.messages.createMessage(MsgKey.ER_RESOURCE_COULD_NOT_LOAD, new Object[] { entitiesResource, e.toString(), entitiesResource, e.toString() }));
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception except) {
                    }
                }
            }
        }
        onlyQuotAmpLtGt = noExtraEntities;
        if (Method.XML.equals(method)) {
            shouldMapTextChar_ASCII[S_QUOTE] = false;
        }
        if (Method.HTML.equals(method)) {
            shouldMapAttrChar_ASCII['<'] = false;
            shouldMapTextChar_ASCII[S_QUOTE] = false;
        }
    }
}
