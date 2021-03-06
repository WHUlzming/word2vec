    @Override

    public void dispose() {

        try {

            if (readerForFaucet != null) {

                if (logger.isTraceEnabled()) {

                    logger.trace("About to close reader: " + readerForFaucet);

                }

                readerForFaucet.close();

            }

        } catch (IOException e) {

            throw new XformationException("Unable to close reader", e);

        }

        faucet.dispose();

    }
