                yield(VM_Scheduler.debuggerQueue, VM_Scheduler.debuggerMutex);

                return;

            default:

                if (tokens.length == 1) {

                    VM.sysWrite("Try one of:\n");

                    VM.sysWrite("   t                - display all threads\n");

                    VM.sysWrite("   t <threadIndex>  - display specified thread\n");

                    VM.sysWrite("   p <hex addr>     - print (describe) object at given address\n");

                    VM.sysWrite("   d                - dump virtual machine state\n");
