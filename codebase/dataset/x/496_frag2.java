                    yPosPVs.add(bpmPV);

                    updatingController.addArrayDataPV(bpmPV.getArrayDataPV());

                }

                for (int i = 0, n = phasePVs.size(); i < n; i++) {

                    BpmViewerPV bpmPV = (BpmViewerPV) phasePVs.get(i);

                    PVTreeNode pvNodeNew = new PVTreeNode(bpmPV.getChannelName());
