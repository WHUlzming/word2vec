    private byte[] signData(byte[] data) {

        DataStore privateDS = new DataStore();

        try {

            privateDS.put(RequestSignature.DS_UNSIGNED_CODE, data);

            new RequestSignature(privateDS, this).init();

        } catch (TimeOutException e) {

            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: " + getName() + ". Error requesting the data signature: " + e);

        }

        return (byte[]) privateDS.remove(RequestSignature.DS_SIGNED_CODE);

    }



    class RequestSignature extends RequestInitiator {



        public RequestSignature(DataStore ds, Agent agent) {

            super(agent);

            _ds = ds;

            _agent = agent;

        }



        @Override

        protected void handleFailureRequest(ACLMessage failure) {

            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestSignature: " + getName() + ". Error signing the agent code: " + failure.getContent());

        }



        @Override

        protected void handleInformRequest(ACLMessage inform) {

            Predicate predicate = null;

            byte[] signature;

            try {

                predicate = (Predicate) _agent.getContentManager().extractContent(inform);

            } catch (Exception e) {

                if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestSignature: " + getName() + ": " + ERR_EXTRACT_CONTENT + e);

            }

            if (predicate != null) {

                if (predicate instanceof InformSignaturePredicate) {

                    signature = ((InformSignaturePredicate) predicate).getSignature();

                    if (signature != null) {

                        _ds.put(DS_SIGNED_CODE, signature);

                    } else {

                        if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestSignature: " + getName() + ": " + ERR_NULL_CONTENT);

                    }

                } else {

                    if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestSignature: " + getName() + ": " + ERR_INCORRECT_ACTION);

                }

            } else {

                if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestSignature: " + getName() + ": " + ERR_NULL_ACTION);

            }

        }



        /**

		 * Method to request the code deciphering.

		 */

        protected ACLMessage prepareInitiation(ACLMessage request) {
