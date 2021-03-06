        } else if (command.equals("stepline") || command.equals("sl")) {

            printMode = PRINTASSEMBLY;

            runstat = user.pstepLine(0, printMode);

            if (runstat == true) refreshEnvironment();

        } else if (command.equals("steplineover") || command.equals("slo")) {

            printMode = PRINTSOURCE;

            runstat = user.pstepLineOverMethod(0);

            if (runstat == true) refreshEnvironment();

        } else if (command.equals("run")) {

            jdp_console.writeOutput("Debuggee is running, kill before restarting");

        } else if (command.equals("kill") || command.equals("k")) {

            switch(debuggerEnvironment) {

                case EXTERNALCREATE:

                    runstat = false;

                    break;

                case EXTERNALATTACH:

                    jdp_console.writeOutput("Cannot kill attached process, type quit to detach debugger");

                    break;

                case INTERNAL:

                    jdp_console.writeOutput("Debugger running inside JVM, type quit to exit debugger");

            }

        } else if (command.equals("cont") || command.equals("c")) {

            if (debuggerEnvironment == EXTERNALATTACH && !user.bpset.anyBreakpointExist()) {

                jdp_console.writeOutput("no breakpoint currently set, detaching process");

                return true;

            } else {

                runstat = user.pcontinue(0, printMode, true);

                if (runstat == true) refreshEnvironment();

            }

        } else if (command.equals("cthread") || command.equals("ct")) {
