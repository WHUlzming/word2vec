    Extern(String packageName, String path, boolean relative) {

        this.packageName = packageName;

        this.path = path;

        this.relative = relative;

        if (packageMap == null) {

            packageMap = new HashMap();

        }

        if (!packageMap.containsKey(packageName)) {

            packageMap.put(packageName, this);

        }

    }
