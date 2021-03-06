    public boolean bringOntologyUpToDate(OWLOntology ontology) throws IOException {

        Long clientVersion = getOntologySequenceNumber(ontology);

        Long serverVersion = queryServer(ontology);

        ArrayList<ChangeCapsule> listOfChanges = new ArrayList<ChangeCapsule>();

        while (clientVersion < serverVersion) {

            clientVersion++;

            fetchServer(ontology, clientVersion);

        }

        return false;

    }
