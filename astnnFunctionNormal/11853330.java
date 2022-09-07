class BackupThread extends Thread {
    public void newProject(final File projectFile, final File targetFile, final TabView view, final URI projectURI, final URI targetURI) {
        assert (projectURI != null);
        if (model != null) {
            if (view.queryUser("Overwrite loaded project?", OVERWRITEPROJECTTITLE) != AbstractView.QueryValues.YES) return;
            model.close();
            model = null;
            setChanged();
        }
        if (projectFile.exists()) {
            if (view.queryUser("Project file already exists; Delete and Overwrite", "Overwrite Project File?") == AbstractView.QueryValues.YES) {
                if (targetFile.exists()) {
                    if (view.queryUser("Delete and Overwrite Target Ontology file as well?", "Overwrite Ontology File?") == AbstractView.QueryValues.YES) {
                        targetFile.delete();
                    } else return;
                }
                projectFile.delete();
            }
        }
        if (targetFile.exists()) {
            if (view.queryUser("An ontology file with the same name as the project's ontology target already exists; Delete?", "Delete Unassociated Ontology") == AbstractView.QueryValues.YES) if (!targetFile.delete()) {
                view.genericError("Existing file (" + targetFile.toString() + ") could not be deleted.");
                return;
            } else return;
        }
        model = ProjectModelFactory.createProjectModel();
        setPropertiesFromModel();
        sourceURI = projectURI;
        setChanged();
        if (projectResource == null) projectResource = model.createResource(sourceURI);
        RDFResourceNode projectNode = model.setProjectURI(projectURI);
        model.setTargetURI(projectNode, targetURI);
        if (sourceURI == null) {
            model.close();
            model = null;
            setChanged();
        }
        targetOntology = new OWOntology(targetFile, IRI.create(targetURI), this, OntType.target, view);
        displayReasoner = projectReasonerFactory.createReasoner(targetOntology.getModel());
        outboardReasoner = projectReasonerFactory.createReasoner(targetOntology.getModel());
        notifyObservers();
    }
}
