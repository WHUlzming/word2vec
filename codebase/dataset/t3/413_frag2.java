            protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {

                OwlsCompositeProcess model = createInitialModel(facade);

                attachModelToResource(model, diagramResource);

                Diagram diagram = ViewService.createDiagram(model, OwlsCompositeProcessEditPart.MODEL_ID, OwlsDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);

                if (diagram != null) {

                    diagramResource.getContents().add(diagram);

                    diagram.setName(diagramName);

                    diagram.setElement(model);

                }

                try {

                    diagramResource.save(owls.diagram.part.OwlsDiagramEditorUtil.getSaveOptions());

                } catch (IOException e) {

                    OwlsDiagramEditorPlugin.getInstance().logError("Unable to store model and diagram resources", e);

                }

                return CommandResult.newOKCommandResult();

            }
