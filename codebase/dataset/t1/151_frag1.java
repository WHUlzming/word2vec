    public static void main(String args[]) throws Exception {

        Getopt g = new Getopt("pdf2dcm.jar", args, "D:");

        Configuration cfg = new Configuration(Pdf2Dcm.class.getResource("pdf2dcm.cfg"));

        int c;

        while ((c = g.getopt()) != -1) {

            switch(c) {

                case 'D':

                    add(cfg, g.getOptarg());

                    break;

                case '?':

                    exit("");

                    break;

            }

        }

        int optind = g.getOptind();

        int argc = args.length - optind;

        if (argc < 2) {

            exit("pdf2dcm.jar: Missing argument\n");

        }

        if (argc > 2) {

            exit("pdf2dcm.jar: To many arguments\n");

        }

        encapsulate(cfg, new File(args[optind]), new File(args[optind + 1]));

    }
