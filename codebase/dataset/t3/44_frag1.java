        boolean today = false;

        for (optind = 0; optind < argv.length; optind++) {

            if (argv[optind].equals("-T")) {

                protocol = argv[++optind];

            } else if (argv[optind].equals("-H")) {

                host = argv[++optind];

            } else if (argv[optind].equals("-U")) {

                user = argv[++optind];

            } else if (argv[optind].equals("-P")) {

                password = argv[++optind];

            } else if (argv[optind].equals("-or")) {

                or = true;

            } else if (argv[optind].equals("-D")) {

                debug = true;

            } else if (argv[optind].equals("-f")) {

                mbox = argv[++optind];

            } else if (argv[optind].equals("-L")) {

                url = argv[++optind];

            } else if (argv[optind].equals("-subject")) {
