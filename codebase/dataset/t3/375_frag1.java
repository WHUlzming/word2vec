    private DragSource createTreeDragSource(final Tree tree) {

        DragSource dragSource = new DragSource(tree, DND.DROP_MOVE | DND.DROP_COPY);

        dragSource.setTransfer(new Transfer[] { FileTransfer.getInstance() });

        dragSource.addDragListener(new DragSourceListener() {



            TreeItem[] dndSelection = null;



            String[] sourceNames = null;



            public void dragStart(DragSourceEvent event) {

                dndSelection = tree.getSelection();

                sourceNames = null;

                event.doit = dndSelection.length > 0;

                isDragging = true;

                processedDropFiles = null;

            }



            public void dragFinished(DragSourceEvent event) {

                dragSourceHandleDragFinished(event, sourceNames);

                dndSelection = null;

                sourceNames = null;

                isDragging = false;

                processedDropFiles = null;

                handleDeferredRefresh();

            }



            public void dragSetData(DragSourceEvent event) {

                if (dndSelection == null || dndSelection.length == 0) return;

                if (!FileTransfer.getInstance().isSupportedType(event.dataType)) return;

                sourceNames = new String[dndSelection.length];

                for (int i = 0; i < dndSelection.length; i++) {

                    File file = (File) dndSelection[i].getData(TREEITEMDATA_FILE);

                    sourceNames[i] = file.getAbsolutePath();

                }

                event.data = sourceNames;

            }

        });

        return dragSource;

    }
