    public AbstractRingtone(pj_pool_t memoryPool, pjsua_media_config mediaConfig, long sampleRate, long samplesPerFrame, List<RingtoneSpecification> ringParams) {

        this.memoryPool = memoryPool;

        this.mediaConfig = mediaConfig;

        this.ringToneSampleRate = sampleRate;

        this.samplesPerFrame = samplesPerFrame;

        this.ringParams = ringParams;

    }
