    public BasicReloadableReference(URL url, long interval, Object value, long timestamp, int status) {

        super(value);

        this.url = url;

        this.interval = interval;

        this.timestamp = timestamp;

        this.status = status;

    }
