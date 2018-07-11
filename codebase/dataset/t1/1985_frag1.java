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
