            if (interpretMode) {

                mapVM.cachePointers();

            }

            user.bmap.fillBootMethodTable();

            if (!viewBoot) {

                goToMainMethod();

            }

        }

    }



    /**

   * progresses the RVM process to the beginning of the

   * user main() method

   */

    private void goToMainMethod() {

        breakpoint bp = null;

        try {

            bp = user.bmap.findBreakpoint("VM.debugBreakpoint", null, user.reg.hardwareIP());
