    public void beginProcessing(SafletContext context) throws ActionStepException {

        super.beginProcessing(context);

        if (call1 == null) {

            handleException(context, new ActionStepException("No current call found"));

            return;

        } else if (!(call1 instanceof Call)) {

            handleException(context, new ActionStepException("Call isn't isn't an Asterisk call: " + call1.getClass().getName()));

            return;

        }

        if (((Call) call1).getChannel() == null) {

            handleException(context, new ActionStepException("No channel found in current context"));

            return;

        }

        AgiChannel channel = ((Call) call1).getChannel();

        try {

            String callerIdStr = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(callerId, context));

            channel.setCallerId(StringUtils.isBlank(callerIdStr) ? "" : callerIdStr);

        } catch (Exception e) {

            handleException(context, e);

            return;

        }

        handleSuccess(context);

    }
