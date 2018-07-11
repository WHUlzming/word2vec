    public SNMPProcessPlotter.Sequence createSequence(String key, String name, Color color, boolean isPlotted) {

        Sequence seq = getSequence(key);

        if (seq == null) {

            seq = new Sequence(key);

        }

        seq.name = name;

        seq.color = (color != null) ? color : defaultColor;

        seq.isPlotted = isPlotted;

        seqs.add(seq);

        return seq;

    }
