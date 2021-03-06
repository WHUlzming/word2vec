    public boolean[] getSubjectUniqueID() {

        if (info == null) return null;

        try {

            UniqueIdentity id = (UniqueIdentity) info.get(CertificateSubjectUniqueIdentity.NAME + DOT + CertificateSubjectUniqueIdentity.ID);

            if (id == null) return null; else return (id.getId());

        } catch (Exception e) {

            return null;

        }

    }
