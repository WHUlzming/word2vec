    public static void main(String args[]) throws Exception {

        Getopt g = new Getopt("DcmGen", args, "", LONG_OPTS);

        Configuration cfg = new Configuration(DcmGen.class.getResource("dcmgen.cfg"));

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 2:

                    cfg.put(LONG_OPTS[g.getLongind()].getName(), g.getOptarg());

                    break;

                case 'P':

                    cfg.put("prior", "1");

                    break;

                case 'p':

                    cfg.put("prior", "2");

                    break;

                case 'k':

                    cfg.put("pack-pdvs", "true");

                    break;

                case 't':

                    cfg.put("trunc-post-pixeldata", "true");

                    break;

                case 'x':

                    cfg.put("exclude-private", "true");

                    break;

                case 's':

                    set(cfg, g.getOptarg());

                    break;

                case 'r':

                    cfg.put("set-random", "true");

                    break;

                case 'c':

                    cfg.put("set-complete", "true");

                    break;

                case 'v':

                    exit(messages.getString("version"), false);

                case 'h':

                    exit(messages.getString("usage"), false);

                case '?':

                    exit(null, true);

                    break;

            }

        }

        int optind = g.getOptind();

        int argc = args.length - optind;

        if (argc == 0) {

            exit(messages.getString("missing"), true);

        }

        try {

            DcmGen DcmGen = new DcmGen(cfg, new DcmURL(args[optind]), argc);

            DcmGen.execute(args, optind + 1);

        } catch (IllegalArgumentException e) {

            exit(e.getMessage(), true);

        }

    }
