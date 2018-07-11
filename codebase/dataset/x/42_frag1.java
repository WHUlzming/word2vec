    protected void setDocumentProvider(IEditorInput input) {

        if (input instanceof IFileEditorInput || input instanceof URIEditorInput) {

            setDocumentProvider(PetriDiagramDebuggerPlugin.getInstance().getDocumentProvider());

        } else {

            super.setDocumentProvider(input);

        }

    }
