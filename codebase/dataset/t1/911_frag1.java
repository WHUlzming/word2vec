    @Override

    public void setActivePoint(final Object data) {

        if (data instanceof IProfilPointMarker) {

            final IProfilPointMarker devider = (IProfilPointMarker) data;

            final IRecord activePoint = devider.getPoint();

            final ProfilOperation operation = new ProfilOperation("", getProfil(), new ActiveObjectEdit(getProfil(), activePoint, null), true);

            final IStatus status = operation.execute(new NullProgressMonitor(), null);

            operation.dispose();

            if (!status.isOK()) KalypsoModelWspmUIPlugin.getDefault().getLog().log(status);

        }

    }
