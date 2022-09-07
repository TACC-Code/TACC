class BackupThread extends Thread {
    public SpringAwareWarBasedAxisConfigurator(ServletConfig servletConfig, ArrayList<ModuleBean> moduleList) throws DeploymentException {
        try {
            this.config = servletConfig;
            this.moduleList = moduleList;
            InputStream axis2Stream = null;
            try {
                String webpath = config.getServletContext().getRealPath("");
                if (webpath == null || webpath.length() == 0) {
                    webpath = config.getServletContext().getRealPath("/");
                }
                if (webpath != null && !"".equals(webpath)) {
                    log.debug("setting web location string: " + webpath);
                    File weblocation = new File(webpath);
                    setWebLocationString(weblocation.getAbsolutePath());
                }
                String axis2xmlpath = config.getInitParameter(PARAM_AXIS2_XML_PATH);
                if (axis2xmlpath != null) {
                    axis2Stream = new FileInputStream(axis2xmlpath);
                    log.debug("using axis2.xml from path: " + axis2xmlpath);
                }
                if (axis2Stream == null) {
                    String axisurl = config.getInitParameter(PARAM_AXIS2_XML_URL);
                    if (axisurl != null) {
                        axis2Stream = new URL(axisurl).openStream();
                        axisConfig = populateAxisConfiguration(axis2Stream);
                        log.debug("loading axis2.xml from URL: " + axisurl);
                    }
                }
                if (axis2Stream == null) {
                    axis2Stream = config.getServletContext().getResourceAsStream("/WEB-INF/conf/axis2.xml");
                    log.debug("trying to load axis2.xml from module: /WEB-INF/conf/axis2.xml");
                }
                if (axis2Stream == null) {
                    axis2Stream = config.getServletContext().getResourceAsStream("/WEB-INF/axis2.xml");
                    log.debug("trying to load axis2.xml from module: /WEB-INF/conf/axis2.xml");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.warn("Using default configuration: " + DeploymentConstants.AXIS2_CONFIGURATION_RESOURCE);
            }
            if (axis2Stream == null) {
                log.info("Could not find axis2.xml, loading default " + DeploymentConstants.AXIS2_CONFIGURATION_RESOURCE + " from classpath");
                axis2Stream = Loader.getResourceAsStream(DeploymentConstants.AXIS2_CONFIGURATION_RESOURCE);
            }
            axisConfig = populateAxisConfiguration(axis2Stream);
            if (axis2Stream != null) {
                axis2Stream.close();
            }
            Parameter param = new Parameter();
            param.setName(Constants.Configuration.ARTIFACTS_TEMP_DIR);
            File f = new File((File) config.getServletContext().getAttribute("javax.servlet.context.tempdir"), "_axis2");
            if (f.exists() || f.mkdirs()) {
                param.setValue(f);
            } else {
                f = new File(System.getProperty("java.io.tmpdir"), "_axis2");
                if (f.exists() || f.mkdirs()) {
                    param.setValue(f);
                } else {
                    throw new DeploymentException("Unable to create a temporary working directory");
                }
            }
            try {
                axisConfig.addParameter(param);
            } catch (AxisFault axisFault) {
                log.error(axisFault.getMessage(), axisFault);
            }
        } catch (DeploymentException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
