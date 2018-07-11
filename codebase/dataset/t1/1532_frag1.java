    private DirBuilderPref getDirBuilderPref() {

        HashMap map = new HashMap();

        for (Enumeration en = cfg.keys(); en.hasMoreElements(); ) {

            addDirBuilderPrefElem(map, (String) en.nextElement());

        }

        DirBuilderPref pref = fact.newDirBuilderPref();

        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {

            Map.Entry entry = (Map.Entry) it.next();

            pref.setFilterForRecordType((String) entry.getKey(), (Dataset) entry.getValue());

        }

        return pref;

    }
