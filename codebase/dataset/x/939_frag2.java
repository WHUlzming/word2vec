    public void addDataSource(final URI uri) {

        if (model == null) {

            model = ProjectModelFactory.createProjectModel();

            setPropertiesFromModel();

        }

        RDFResourceNode targetResource = model.createResource(uri);

        model.addTriple(projectResource, annotatesDataProperty, targetResource);

        setChanged();

        notifyObservers();

    }
