    public void addSecondaryZone(String zone, String remote) throws IOException {

        Cache cache = getCache(DClass.IN);

        Name zname = Name.fromString(zone, Name.root);

        Zone newzone = new Zone(zname, DClass.IN, remote, cache);

        znames.put(zname, newzone);

    }
