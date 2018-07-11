    public boolean pstepLineOverMethod(int thread) {

        boolean stillrunning = true;

        boolean skip_prolog;

        int addr, orig_frame, curr_frame;

        String orig_line;

        String curr_line;

        orig_frame = mem.findFrameCount();

        stillrunning = pstepLine(thread, PRINTNONE);

        if (!stillrunning) return stillrunning;

        curr_frame = mem.findFrameCount();

        if (orig_frame < curr_frame) {

            stillrunning = pcontinueToReturn(thread, PRINTNONE);

        } else {

            stillrunning = true;

        }

        if (stillrunning) printCurrentStatus(PRINTSOURCE);

        return stillrunning;

    }
