    public void stop() {

        final Tracer tracer = baseTracer.entering("stop()");

        try {

            channelConfiguration.stop();

        } catch (Exception e) {

            tracer.catched(e);

            tracer.error("Channel monitoring deregistration failed..." + e.toString());

        }

        tracer.leaving();

    }
