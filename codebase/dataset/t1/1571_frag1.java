    public Cache getCache(int dclass) {

        Cache c = (Cache) caches.get(new Integer(dclass));

        if (c == null) {

            c = new Cache(dclass);

            caches.put(new Integer(dclass), c);

        }

        return c;

    }



    public Zone findBestZone(Name name) {

        Zone foundzone = null;

        foundzone = (Zone) znames.get(name);

        if (foundzone != null) return foundzone;

        int labels = name.labels();

        for (int i = 1; i < labels; i++) {

            Name tname = new Name(name, i);

            foundzone = (Zone) znames.get(tname);

            if (foundzone != null) return foundzone;

        }

        return null;

    }



    public RRset findExactMatch(Name name, int type, int dclass, boolean glue) {

        Zone zone = findBestZone(name);

        if (zone != null) return zone.findExactMatch(name, type); else {

            RRset[] rrsets;

            Cache cache = getCache(dclass);

            if (glue) rrsets = cache.findAnyRecords(name, type); else rrsets = cache.findRecords(name, type);

            if (rrsets == null) return null; else return rrsets[0];

        }

    }



    void addRRset(Name name, Message response, RRset rrset, byte section, int flags) {
