        if (m_programData != null) return m_programData.getProgramsSortedByTime(); else return null;

    }



    public ProgramList getEmptyProgramList() {

        if (m_programData != null) return m_programData.getEmptyProgramList(); else return null;

    }



    public void formatProgs(Writer out, boolean html) throws IOException {

        ProgramList progs = getPrograms();

        formatProgs(progs, out, html);
