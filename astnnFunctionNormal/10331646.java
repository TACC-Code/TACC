class BackupThread extends Thread {
    public void execute() throws MojoExecutionException {
        if (warDirectory.exists()) {
            FileUtils.delete(warDirectory);
        }
        warDirectory.mkdir();
        File webInfDir = new File(warDirectory, "WEB-INF");
        webInfDir.mkdir();
        File metaInfDir = new File(warDirectory, "META-INF");
        metaInfDir.mkdir();
        File eclipseDir = new File(webInfDir, "eclipse");
        eclipseDir.mkdir();
        File pluginsDir = new File(eclipseDir, "plugins");
        pluginsDir.mkdir();
        File configDir = new File(eclipseDir, "configuration");
        configDir.mkdir();
        File libDir = new File(webInfDir, "lib");
        libDir.mkdir();
        if (contextXMLFile != null && contextXMLFile.exists()) {
            try {
                URL contextXMLFileURL = contextXMLFile.toURI().toURL();
                copyFile(contextXMLFileURL, new File(metaInfDir, "context.xml"));
            } catch (IOException x) {
                throw new MojoExecutionException("Can't copy context.xml", x);
            }
        }
        URL[] provisioningConfigs;
        try {
            provisioningConfigs = new URL[] { provisioningConfig.toURI().toURL() };
        } catch (MalformedURLException x) {
            throw new MojoExecutionException("Illegal provisioning configuration URL", x);
        }
        BundleResolver bundleResolver = new BundleResolver(provisioningConfigs, defaultStartLevel, mavenProject, artifactFactory, resolver, metadataSource, localRepository, remoteRepositories, getLog());
        List<BundleConfig> bundles = bundleResolver.resolve();
        for (BundleConfig bundle : bundles) {
            File source = bundle.getFile();
            if (source.isDirectory()) {
                File targetDir = new File(mavenProject.getBasedir(), "target");
                source = new File(targetDir, mavenProject.getArtifactId() + "-" + mavenProject.getVersion() + ".jar");
                BundleConfig bc = new BundleConfig(source, bundle.getBundleSymbolicName(), true, 4);
                bundles.set(bundles.indexOf(bundle), bc);
            }
            File target = new File(pluginsDir, source.getName());
            try {
                if (!bundle.getBundleSymbolicName().contains(JETTY_BUNDLE_SYMBOLIC_NAME)) FileUtils.copyFile(source, target);
            } catch (IOException x) {
                throw new MojoExecutionException("Can't copy bundle: " + bundle.getFile().getAbsolutePath(), x);
            }
        }
        URL webXml = getClass().getResource("/eclipse/web.xml");
        try {
            copyFile(webXml, new File(webInfDir, "web.xml"));
        } catch (IOException x) {
            throw new MojoExecutionException("Can't copy web.xml", x);
        }
        URL launchIni = getClass().getResource("/eclipse/launch.ini");
        try {
            copyFile(launchIni, new File(webInfDir, "launch.ini"));
        } catch (IOException x) {
            throw new MojoExecutionException("Can't copy launch.ini", x);
        }
        URL servletBridge = getClass().getResource("/eclipse/servletbridge.jar");
        try {
            copyFile(servletBridge, new File(libDir, "servletbridge.jar"));
        } catch (IOException x) {
            throw new MojoExecutionException("Can't copy servletbridge.jar", x);
        }
        InputStream in = getClass().getResourceAsStream("/eclipse/bundles.list");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                URL bundle = getClass().getResource("/eclipse/bundles/" + line);
                copyFile(bundle, new File(pluginsDir, line));
            }
            reader.close();
        } catch (IOException x) {
            throw new MojoExecutionException("Error reading bundle list", x);
        }
        StringBuilder config = new StringBuilder();
        config.append("osgi.bundles.defaultStartLevel=4\n");
        config.append("osgi.clean=true\n");
        config.append("org.osgi.supports.framework.fragment=true\n");
        config.append("osgi.bundles=");
        config.append("org.eclipse.equinox.common@2:start,org.eclipse.osgi.services,org.eclipse.equinox.servletbridge.extensionbundle,org.eclipse.equinox.http.servlet,org.eclipse.equinox.http.servletbridge@3:start,");
        Iterator<BundleConfig> it = bundles.iterator();
        while (it.hasNext()) {
            BundleConfig bundle = it.next();
            if (!bundle.getBundleSymbolicName().equals(FRAMEWORK_BUNDLE_SYMBOLIC_NAME) && !bundle.getBundleSymbolicName().contains(JETTY_BUNDLE_SYMBOLIC_NAME)) {
                config.append(bundle.getFile().getName());
                if (bundle.doStart()) {
                    if (bundle.getStartLevel() == 4) config.append("@start"); else config.append("@" + bundle.getStartLevel() + ":start");
                }
                if (it.hasNext()) config.append(",");
            }
        }
        config.append("\n");
        try {
            File configFile = new File(configDir, "config.ini");
            FileOutputStream out = new FileOutputStream(configFile);
            out.write(config.toString().getBytes("UTF-8"));
            out.close();
        } catch (IOException x) {
            throw new RuntimeException("Error creating Equinox configuration", x);
        }
        try {
            FileOutputStream out = new FileOutputStream(warFile);
            JarOutputStream jarOut = new JarOutputStream(out);
            addJarEntry(jarOut, warDirectory, warDirectory);
            jarOut.close();
        } catch (IOException x) {
            throw new MojoExecutionException("Error writing war file", x);
        }
    }
}
