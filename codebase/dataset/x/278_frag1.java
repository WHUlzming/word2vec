    private final void addCacheNS(Message response, Cache cache, Name name) {

        SetResponse sr = cache.lookupRecords(name, Type.NS, Credibility.HINT);

        if (!sr.isDelegation()) return;

        RRset nsRecords = sr.getNS();

        Iterator it = nsRecords.rrs();

        while (it.hasNext()) {

            Record r = (Record) it.next();

            response.addRecord(r, Section.AUTHORITY);

        }

    }
