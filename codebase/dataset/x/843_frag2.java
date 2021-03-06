    protected void writeStrandedFeatureSet(PrintWriter pw, StrandedFeatureSetI sfs) {

        pw.println("begin StrandedFeatureSet");

        FeatureSetI fs = sfs.getForwardSet();

        pw.println("forward=begin forward()");

        writeFeatureSet(pw, fs);

        pw.println("end forward\n");

        FeatureSetI rs = sfs.getReverseSet();

        pw.println("reverse=begin reverse()");

        writeFeatureSet(pw, rs);

        pw.println("end reverse\n");

        pw.println("end StrandedFeatureSet");
