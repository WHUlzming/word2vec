            runstat = user.pcontinueToReturn(0, printMode);

            if (runstat == true) refreshEnvironment();

        } else if (command.equals("thread") || command.equals("th")) {

            doThread(command, args);

        } else if (command.equals("reg") || command.equals("r")) {

            doRegisterRead(command, args);

        } else if (command.equals("wreg") || command.equals("wr")) {

            doRegisterWrite(command, args);

        } else if (command.equals("memraw") || command.equals("mraw")) {

            doMemoryReadRaw(command, args);

        } else if (command.equals("mem") || command.equals("m")) {

            doMemoryRead(command, args);

        } else if (command.equals("wmem") || command.equals("wm")) {

            doMemoryWrite(command, args);

        } else if (command.equals("print") || command.equals("p")) {

            doPrintCommand(command, args);

        } else if (command.equals("printclass") || command.equals("pc")) {

            doPrintClassCommand(command, args);

        } else if (command.equals("getclass")) {

            doGetClassCommand(command, args);

        } else if (command.equals("getinstance")) {

            doGetInstanceCommand(command, args);

        } else if (command.equals("getarray")) {

            doGetArrayCommand(command, args);

        } else if (command.equals("getcl")) {

            doGetClassAndLine(command, args);

        } else if (command.equals("getcia")) {

            doGetCurrentInstrAddr(command, args);

        } else if (command.equals("getframes")) {

            doGetFrames(command, args);

        } else if (command.equals("getlocals")) {

            doGetLocals(command, args);

        } else if (command.equals("listb") || command.equals("lb")) {

            jdp_console.writeOutput("(this command has been removed because the Opt compiler does not generate the bytecode map)");

        } else if (command.equals("listi") || command.equals("li")) {

            doListInstruction(command, args);

        } else if (command.equals("listt") || command.equals("lt")) {

            doListThread(command, args);

        } else if (command.equals("break") || command.equals("b")) {

            doSetBreakpoint(command, args);

        } else if (command.equals("clearbreak") || command.equals("cb")) {

            doClearBreakpoint(command, args);

        } else if (command.equals("stack") || command.equals("f")) {

            doCurrentFrame(command, args);

        } else if (command.equals("where") || command.equals("w")) {

            doShortFrame(command, args);

        } else if (command.equals("whereframe") || command.equals("wf")) {

            doFullFrame(command, args);

        } else if (command.equals("preference") || command.equals("pref")) {

            doSetPreference(command, args);

        } else if (command.equals("preference") || command.equals("x2d")) {

            doConvertHexToInt(command, args);

        } else if (command.equals("preference") || command.equals("d2x")) {

            doConvertIntToHex(command, args);

        } else if (command.equals("test")) {

            doTest(args);

        } else if (command.equals("test1")) {

            doTest1(args);

        } else if (command.equals("count")) {

            doThreadCount(0);

        } else if (command.equals("zerocount")) {

            doThreadCount(1);

        } else if (command.equals("readmem")) {

            if (args.length != 0) {

                try {

                    addr = parseHex32(args[0]);

                    int mydata = user.mem.read(addr);

                    jdp_console.writeOutput("true memory = x" + Integer.toHexString(mydata));

                } catch (NumberFormatException e) {

                    jdp_console.writeOutput("bad address: " + args[0]);

                }

            }

        } else if (command.equals("verbose") || command.equals("v")) {

            if (user.verbose) {
