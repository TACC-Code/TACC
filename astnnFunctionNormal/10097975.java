class BackupThread extends Thread {
    private MessagesBundle loadDefaultMessagesBundle() throws IOException, CoreException {
        IEditorInput newEditorInput = null;
        IPath propertiesBasePath = getPropertiesPath();
        String resourceLocationLabel = null;
        IProject developpedProject = getHostPluginProjectDevelopedInWorkspace();
        if (developpedProject != null) {
            IFile file = getPropertiesFile(developpedProject, propertiesBasePath);
            if (!file.exists()) {
                String[] jarredProps = getJarredPropertiesAndResourceLocationLabel(developpedProject, propertiesBasePath);
                if (jarredProps != null) {
                    if (site == null) {
                        MsgEditorPreferences prefs = MsgEditorPreferences.getInstance();
                        return new MessagesBundle(new PropertiesReadOnlyResource(UIUtils.ROOT_LOCALE, new PropertiesSerializer(prefs), new PropertiesDeserializer(prefs), jarredProps[0], jarredProps[1]));
                    }
                    newEditorInput = new DummyEditorInput(jarredProps[0], getPropertiesPath().lastSegment(), jarredProps[1]);
                    resourceLocationLabel = jarredProps[1];
                }
            }
            if (site == null) {
                if (file.exists()) {
                    MsgEditorPreferences prefs = MsgEditorPreferences.getInstance();
                    return new MessagesBundle(new PropertiesIFileResource(UIUtils.ROOT_LOCALE, new PropertiesSerializer(prefs), new PropertiesDeserializer(prefs), file, MessagesEditorPlugin.getDefault()));
                } else {
                    return null;
                }
            }
            if (file.exists()) {
                newEditorInput = new FileEditorInput(file);
            }
        }
        if (newEditorInput == null) {
            InputStream in = null;
            String resourceName = null;
            try {
                Bundle bundle = Platform.getBundle(_fragmentHostID);
                if (bundle != null) {
                    resourceName = propertiesBasePath.toString();
                    URL url = bundle.getEntry(resourceName);
                    if (url != null) {
                        in = url.openStream();
                        resourceLocationLabel = url.toExternalForm();
                    } else {
                        url = bundle.getResource(resourceName);
                        if (url != null) {
                            in = url.openStream();
                            resourceLocationLabel = url.toExternalForm();
                        }
                    }
                }
                if (in != null) {
                    String contents = getContents(in);
                    if (site == null) {
                        MsgEditorPreferences prefs = MsgEditorPreferences.getInstance();
                        return new MessagesBundle(new PropertiesReadOnlyResource(UIUtils.ROOT_LOCALE, new PropertiesSerializer(prefs), new PropertiesDeserializer(prefs), contents, resourceLocationLabel));
                    }
                    newEditorInput = new DummyEditorInput(contents, getPropertiesPath().lastSegment(), getPropertiesPath().toString());
                }
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException ioe) {
                }
            }
        }
        if (newEditorInput != null) {
            TextEditor textEditor = null;
            if (site != null) {
                try {
                    textEditor = (TextEditor) Class.forName(PROPERTIES_EDITOR_CLASS_NAME).newInstance();
                } catch (Exception e) {
                    textEditor = new TextEditor();
                }
                textEditor.init(site, newEditorInput);
            } else {
                System.err.println("the site " + site);
            }
            MsgEditorPreferences prefs = MsgEditorPreferences.getInstance();
            EclipsePropertiesEditorResource readOnly = new EclipsePropertiesEditorResource(UIUtils.ROOT_LOCALE, new PropertiesSerializer(prefs), new PropertiesDeserializer(prefs), textEditor);
            if (resourceLocationLabel != null) {
                readOnly.setResourceLocationLabel(resourceLocationLabel);
            }
            return new MessagesBundle(readOnly);
        }
        return null;
    }
}
