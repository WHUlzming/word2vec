    protected boolean readEphCoeff(double jultime) {

        boolean result = false;

        if ((jultime < this.startepoch) || (jultime >= this.finalepoch)) {

            return result;

        }

        if ((jultime < this.ephemerisdates[1]) || (jultime >= this.ephemerisdates[2])) {

            int i = 0;
