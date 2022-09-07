class BackupThread extends Thread {
    @Override
    public Model getModel(final ModelContext context) throws BuildException, ModelException {
        if (context == null) {
            throw new NullPointerException("context");
        }
        Model model = new Model();
        model.setIdentifier(this.getModel());
        Modules modules = new Modules();
        ModelHelper.setModules(model, modules);
        Unmarshaller unmarshaller = null;
        for (ResourceType resource : this.getModuleResources()) {
            final URL[] urls = this.getResources(context, resource.getLocation());
            if (urls.length == 0) {
                if (resource.isOptional()) {
                    this.logMessage(Level.WARNING, Messages.getMessage("moduleResourceNotFound", resource.getLocation()));
                } else {
                    throw new BuildException(Messages.getMessage("moduleResourceNotFound", resource.getLocation()), this.getLocation());
                }
            }
            for (int i = urls.length - 1; i >= 0; i--) {
                InputStream in = null;
                boolean suppressExceptionOnClose = true;
                try {
                    this.logMessage(Level.FINEST, Messages.getMessage("reading", urls[i].toExternalForm()));
                    final URLConnection con = urls[i].openConnection();
                    con.setConnectTimeout(resource.getConnectTimeout());
                    con.setReadTimeout(resource.getReadTimeout());
                    con.connect();
                    in = con.getInputStream();
                    final Source source = new StreamSource(in, urls[i].toURI().toASCIIString());
                    if (unmarshaller == null) {
                        unmarshaller = context.createUnmarshaller(this.getModel());
                        if (this.isModelResourceValidationEnabled()) {
                            unmarshaller.setSchema(context.createSchema(this.getModel()));
                        }
                    }
                    Object o = unmarshaller.unmarshal(source);
                    if (o instanceof JAXBElement<?>) {
                        o = ((JAXBElement<?>) o).getValue();
                    }
                    if (o instanceof Module) {
                        modules.getModule().add((Module) o);
                    } else {
                        this.log(Messages.getMessage("unsupportedModuleResource", urls[i].toExternalForm()), Project.MSG_WARN);
                    }
                    suppressExceptionOnClose = false;
                } catch (final SocketTimeoutException e) {
                    String message = Messages.getMessage(e);
                    message = Messages.getMessage("resourceTimeout", message != null ? " " + message : "");
                    if (resource.isOptional()) {
                        this.getProject().log(message, e, Project.MSG_WARN);
                    } else {
                        throw new BuildException(message, e, this.getLocation());
                    }
                } catch (final IOException e) {
                    String message = Messages.getMessage(e);
                    message = Messages.getMessage("resourceFailure", message != null ? " " + message : "");
                    if (resource.isOptional()) {
                        this.getProject().log(message, e, Project.MSG_WARN);
                    } else {
                        throw new BuildException(message, e, this.getLocation());
                    }
                } catch (final URISyntaxException e) {
                    throw new BuildException(Messages.getMessage(e), e, this.getLocation());
                } catch (final JAXBException e) {
                    String message = Messages.getMessage(e);
                    if (message == null) {
                        message = Messages.getMessage(e.getLinkedException());
                    }
                    throw new BuildException(message, e, this.getLocation());
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (final IOException e) {
                        if (suppressExceptionOnClose) {
                            this.logMessage(Level.SEVERE, Messages.getMessage(e), e);
                        } else {
                            throw new BuildException(Messages.getMessage(e), e, this.getLocation());
                        }
                    }
                }
            }
        }
        model = context.findModel(model);
        modules = ModelHelper.getModules(model);
        if (modules != null && this.isModelObjectClasspathResolutionEnabled()) {
            final Module classpathModule = modules.getClasspathModule(Modules.getDefaultClasspathModuleName(), context.getClassLoader());
            if (classpathModule != null && modules.getModule(Modules.getDefaultClasspathModuleName()) == null) {
                modules.getModule().add(classpathModule);
            }
        }
        if (this.isModelProcessingEnabled()) {
            model = context.processModel(model);
        }
        return model;
    }
}
