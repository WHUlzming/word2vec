    public void send(String[] args, int offset) throws InterruptedException, IOException, GeneralSecurityException {

        if (bufferSize > 0) {

            buffer = new byte[bufferSize];

        }

        long t1 = System.currentTimeMillis();

        for (int i = 0; i < repeatWhole; ++i) {

            ActiveAssociation active = openAssoc();

            if (active != null) {

                for (int k = offset; k < args.length; ++k) {

                    Dataset ds = null;

                    if (random) {

                        ds = datasets[rnd.nextInt(datasets.length)];

                        if (ds == null) ds = overwrite;

                        ds.putUI(Tags.StudyInstanceUID, uidGen.createUID());

                        ds.putUI(Tags.SeriesInstanceUID, uidGen.createUID());

                    } else if (complete) {

                        ds = datasets[(k - offset) % datasets.length];

                        if (ds == null) ds = overwrite;

                    } else if (datasets.length > (k - offset) && datasets[(k - offset)] != null) {

                        ds = datasets[(k - offset)];

                    }

                    if (ds != null) {

                        log.info("Used dataset for FILE/DIR " + args[k] + " :");

                        logDataset(ds);

                    }

                    send(active, new File(args[k]), ds);

                }

                active.release(true);

            }

        }

        long dt = System.currentTimeMillis() - t1;

        log.info(MessageFormat.format(messages.getString("sendDone"), new Object[] { new Integer(sentCount), new Long(sentBytes), new Long(dt), new Float(sentBytes / (1.024f * dt)) }));

    }
