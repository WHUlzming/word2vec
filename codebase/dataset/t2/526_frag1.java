    public _DataTriggerTable getDataTriggerTable(String databaseURL, QualifiedIdentifier tableName) throws DException {

        if (!isServerActive) throw new DException("DSE2023", null);

        _DataTriggerDatabase dataTriggerDatabase = dataTriggerSystem.getDataTrigerDatabase(databaseURL);

        return dataTriggerDatabase.getDataTriggerTable(tableName);

    }



    public _DataTriggerTable getDataViewTriggerTable(String databaseURL, QualifiedIdentifier tableName) throws DException {
