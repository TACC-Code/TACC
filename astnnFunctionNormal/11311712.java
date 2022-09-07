class BackupThread extends Thread {
    @Override
    public void execute() throws BuildException {
        try {
            GlobalConfigurator.setDocroot(prjdir.getAbsolutePath());
        } catch (IllegalStateException e) {
        }
        try {
            DirectoryScanner scanner = getDirectoryScanner(prjdir);
            String[] files = scanner.getIncludedFiles();
            wsgenDirs = new HashSet<File>();
            for (String file : files) {
                File prjFile = new File(prjdir, file);
                File confDir = prjFile.getParentFile();
                String projectName = confDir.getParentFile().getName();
                Document doc = TaskUtils.loadDoc(prjFile);
                Element serviceElem = (Element) doc.getElementsByTagName("webservice-service").item(0);
                if (serviceElem != null) {
                    Configuration srvConf = null;
                    Element configFileElem = (Element) serviceElem.getElementsByTagName("config-file").item(0);
                    if (configFileElem == null) throw new BuildException("The 'webservice-service' element requires " + " a 'config-file' child element in file '" + prjFile.getAbsolutePath() + "'.");
                    String configFileUri = configFileElem.getTextContent().trim();
                    FileResource configFile = ResourceUtil.getFileResource(configFileUri);
                    if (configFile.exists()) {
                        srvConf = ConfigurationReader.read(configFile);
                    } else {
                        srvConf = new Configuration();
                    }
                    File springConfigFile = new File(prjFile.getParentFile(), "spring.xml");
                    FileResource springConfigRes = ResourceUtil.getFileResource(springConfigFile.toURI());
                    if (springConfigFile.exists()) {
                        List<ServiceConfig> serviceList = WebServiceBeanConfigReader.read(springConfigRes);
                        srvConf.addServiceConfigs(serviceList);
                    }
                    if (srvConf.getServiceConfig().size() > 0) {
                        int wsdlCount = 0;
                        int stubCount = 0;
                        File tmpDir = getTmpDir(projectName);
                        GlobalServiceConfig globConf = srvConf.getGlobalServiceConfig();
                        Configuration refSrvConf = null;
                        GlobalServiceConfig refGlobConf = null;
                        boolean globalConfChanged = false;
                        FileResource refWsConfFile = ResourceUtil.getFileResource("file://" + tmpDir.getAbsolutePath() + "/" + "webservice.conf.ser");
                        if (refWsConfFile.exists()) {
                            try {
                                refSrvConf = ConfigurationReader.deserialize(refWsConfFile);
                                refGlobConf = refSrvConf.getGlobalServiceConfig();
                                if (!globConf.equals(refGlobConf)) globalConfChanged = true;
                            } catch (Exception x) {
                                log("Error deserializing old reference configuration", Project.MSG_VERBOSE);
                                log("Warning: Ignore old reference configuration because it can't be deserialized. " + "Services will be built from scratch.", Project.MSG_WARN);
                            }
                        }
                        File appDir = new File(webappsdir, projectName);
                        if (!appDir.exists()) throw new BuildException("Web application directory of project '" + projectName + "' doesn't exist");
                        File wsdlDir = tmpDir;
                        if (globConf.getWSDLSupportEnabled()) {
                            String wsdlRepo = globConf.getWSDLRepository();
                            if (wsdlRepo.startsWith("/")) wsdlRepo.substring(1);
                            wsdlDir = new File(appDir, wsdlRepo);
                            if (!wsdlDir.exists()) {
                                boolean ok = wsdlDir.mkdir();
                                if (!ok) throw new BuildException("Can't create WSDL directory " + wsdlDir.getAbsolutePath());
                            }
                        }
                        File stubDir = tmpDir;
                        if (globConf.getStubGenerationEnabled()) {
                            String stubRepo = globConf.getStubRepository();
                            if (stubRepo.startsWith("/")) stubRepo.substring(1);
                            stubDir = new File(appDir, stubRepo);
                            if (!stubDir.exists()) {
                                boolean ok = stubDir.mkdir();
                                if (!ok) throw new BuildException("Can't create webservice stub directory " + stubDir.getAbsolutePath());
                            }
                        }
                        File webInfDir = new File(appDir, "WEB-INF");
                        if (!webInfDir.exists()) throw new BuildException("Web application WEB-INF subdirectory of project '" + projectName + "' doesn't exist");
                        for (ServiceConfig conf : srvConf.getServiceConfig()) {
                            if (conf.getProtocolType().equals(Constants.PROTOCOL_TYPE_ANY) || conf.getProtocolType().equals(Constants.PROTOCOL_TYPE_SOAP)) {
                                ServiceConfig refConf = null;
                                if (refSrvConf != null) refConf = refSrvConf.getServiceConfig(conf.getName());
                                File wsdlFile = new File(wsdlDir, conf.getName() + ".wsdl");
                                if (refConf == null || !wsdlFile.exists() || globalConfChanged || !conf.equals(refConf) || TaskUtils.checkInterfaceChange(conf.getInterfaceName(), builddir, wsdlFile)) {
                                    if (conf.getInterfaceName() != null) checkInterface(conf.getInterfaceName());
                                    Class<?> implClass = Class.forName(conf.getImplementationName());
                                    WebService anno = implClass.getAnnotation(WebService.class);
                                    if (anno == null) {
                                        throw new BuildException("Missing @WebService annotation at service implementation " + "class '" + conf.getImplementationName() + "' of service '" + conf.getName() + "'.");
                                    }
                                    File wsgenDir = new File(tmpdir, "wsdl/" + conf.getName() + "/" + conf.getImplementationName());
                                    if (!wsgenDirs.contains(wsgenDir)) {
                                        if (!wsgenDir.exists()) wsgenDir.mkdirs();
                                        WsGen wsgen = new WsGen();
                                        wsgen.setProject(getProject());
                                        wsgen.setDynamicAttribute("keep", "true");
                                        wsgen.setDynamicAttribute("sourcedestdir", "gensrc");
                                        wsgen.setDynamicAttribute("genwsdl", "true");
                                        wsgen.setDynamicAttribute("destdir", "build");
                                        wsgen.setDynamicAttribute("resourcedestdir", wsgenDir.getAbsolutePath());
                                        wsgen.setDynamicAttribute("classpath", classPath.toString());
                                        wsgen.setDynamicAttribute("sei", conf.getImplementationName());
                                        String serviceName = "{" + TaskUtils.getTargetNamespace(implClass) + "}" + conf.getName();
                                        wsgen.setDynamicAttribute("servicename", serviceName);
                                        wsgen.execute();
                                        wsgenDirs.add(wsgenDir);
                                    }
                                    FileUtils.copyFiles(wsgenDir, wsdlDir, ".*wsdl", ".*xsd");
                                    String srvName = "HOST";
                                    String srvPort = "";
                                    if (standalone) srvPort = ":" + (portbase + 80);
                                    String wsUrl = "http://" + srvName + srvPort + globConf.getRequestPath() + "/" + conf.getName();
                                    FileUtils.searchAndReplace(wsdlFile, "UTF-8", "REPLACE_WITH_ACTUAL_URL", wsUrl);
                                    wsdlCount++;
                                    if (globConf.getStubGenerationEnabled()) {
                                        File stubFile = new File(stubDir, conf.getName() + ".js");
                                        if (!stubFile.exists() || stubFile.lastModified() < wsdlFile.lastModified()) {
                                            Wsdl2Js task = new Wsdl2Js();
                                            task.setInputFile(wsdlFile);
                                            task.setOutputFile(stubFile);
                                            task.generate();
                                            stubCount++;
                                        }
                                    }
                                }
                            }
                        }
                        if (wsdlCount > 0) log("Generated " + wsdlCount + " WSDL file" + (wsdlCount == 1 ? "" : "s") + ".");
                        if (stubCount > 0) log("Generated " + stubCount + " Javascript stub file" + (stubCount == 1 ? "" : "s") + ".");
                        ConfigurationReader.serialize(srvConf, refWsConfFile);
                    }
                }
            }
        } catch (Exception x) {
            throw new BuildException(x);
        }
    }
}
