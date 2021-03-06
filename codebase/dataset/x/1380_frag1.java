            exit(e.getMessage(), true);

        }

    }



    DcmSnd(Configuration cfg, DcmURL url, int argc) {

        this.url = url;

        this.priority = Integer.parseInt(cfg.getProperty("prior", "0"));

        this.packPDVs = "true".equalsIgnoreCase(cfg.getProperty("pack-pdvs", "false"));

        this.truncPostPixelData = "true".equalsIgnoreCase(cfg.getProperty("trunc-post-pixeldata", "false"));

        this.excludePrivate = "true".equalsIgnoreCase(cfg.getProperty("exclude-private", "false"));

        this.bufferSize = Integer.parseInt(cfg.getProperty("buf-len", "2048")) & 0xfffffffe;

        this.repeatWhole = Integer.parseInt(cfg.getProperty("repeat-assoc", "1"));

        this.repeatSingle = Integer.parseInt(cfg.getProperty("repeat-dimse", "1"));

        this.uidSuffix = cfg.getProperty("uid-suffix");

        this.mode = argc > 1 ? SEND : initPollDirSrv(cfg) ? POLL : ECHO;

        initAssocParam(cfg, url, mode == ECHO);

        initTLS(cfg);

        initOverwrite(cfg);

    }



    public void execute(String[] args, int offset) throws InterruptedException, IOException, GeneralSecurityException {

        switch(mode) {

            case ECHO:

                echo();

                break;

            case SEND:

                send(args, offset);

                break;

            case POLL:

                poll();

                break;

            default:

                throw new RuntimeException("Illegal mode: " + mode);

        }

    }



    private ActiveAssociation openAssoc() throws IOException, GeneralSecurityException {

        Association assoc = aFact.newRequestor(newSocket(url.getHost(), url.getPort()));

        assoc.setAcTimeout(acTimeout);

        assoc.setDimseTimeout(dimseTimeout);

        assoc.setSoCloseDelay(soCloseDelay);

        assoc.setPackPDVs(packPDVs);

        PDU assocAC = assoc.connect(assocRQ);

        if (!(assocAC instanceof AAssociateAC)) {

            return null;

        }

        ActiveAssociation retval = aFact.newActiveAssociation(assoc, null);

        retval.start();

        return retval;

    }



    public void echo() throws InterruptedException, IOException, GeneralSecurityException {

        long t1 = System.currentTimeMillis();

        int count = 0;

        for (int i = 0; i < repeatWhole; ++i) {

            ActiveAssociation active = openAssoc();

            if (active != null) {

                if (active.getAssociation().getAcceptedTransferSyntaxUID(PCID_ECHO) == null) {

                    log.error(messages.getString("noPCEcho"));

                } else for (int j = 0; j < repeatSingle; ++j, ++count) {

                    active.invoke(aFact.newDimse(PCID_ECHO, oFact.newCommand().initCEchoRQ(j)), null);

                }

                active.release(true);

            }

        }

        long dt = System.currentTimeMillis() - t1;

        log.info(MessageFormat.format(messages.getString("echoDone"), new Object[] { new Integer(count), new Long(dt) }));

    }



    public void send(String[] args, int offset) throws InterruptedException, IOException, GeneralSecurityException {

        if (bufferSize > 0) {

            buffer = new byte[bufferSize];

        }

        long t1 = System.currentTimeMillis();

        for (int i = 0; i < repeatWhole; ++i) {

            ActiveAssociation active = openAssoc();

            if (active != null) {

                for (int k = offset; k < args.length; ++k) {

                    send(active, new File(args[k]));

                }

                active.release(true);

            }

        }

        long dt = System.currentTimeMillis() - t1;

        log.info(MessageFormat.format(messages.getString("sendDone"), new Object[] { new Integer(sentCount), new Long(sentBytes), new Long(dt), new Float(sentBytes / (1.024f * dt)) }));

    }



    public void poll() {

        pollDirSrv.start(pollDir, pollPeriod);

    }



    public void openSession() throws Exception {

        activeAssociation = openAssoc();

        if (activeAssociation == null) {

            throw new IOException("Could not open association");

        }

    }



    public boolean process(File file) throws Exception {

        return sendFile(activeAssociation, file);

    }



    public void closeSession() {

        if (activeAssociation != null) {

            try {

                activeAssociation.release(true);

            } catch (Exception e) {

                log.warn("release association throws:", e);

            }

            activeAssociation = null;

        }

    }



    private void send(ActiveAssociation active, File file) throws InterruptedException, IOException {

        if (!file.isDirectory()) {

            for (int i = 0; i < repeatSingle; ++i) {

                sendFile(active, file);

            }

            return;

        }

        File[] list = file.listFiles();

        for (int i = 0; i < list.length; ++i) {

            send(active, list[i]);

        }

    }



    private boolean sendFile(ActiveAssociation active, File file) throws InterruptedException, IOException {

        InputStream in = null;

        DcmParser parser = null;

        Dataset ds = null;

        try {

            try {

                in = new BufferedInputStream(new FileInputStream(file));

                parser = pFact.newDcmParser(in);
