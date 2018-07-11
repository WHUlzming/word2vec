    void extract(String fname, String files[]) throws IOException {

        ZipFile zf = new ZipFile(fname);

        Set<ZipEntry> dirs = newDirSet();

        Enumeration<? extends ZipEntry> zes = zf.entries();

        while (zes.hasMoreElements()) {

            ZipEntry e = zes.nextElement();

            InputStream is;

            if (files == null) {

                dirs.add(extractFile(zf.getInputStream(e), e));

            } else {

                String name = e.getName();

                for (String file : files) {

                    if (name.startsWith(file)) {

                        dirs.add(extractFile(zf.getInputStream(e), e));

                        break;

                    }

                }

            }

        }

        zf.close();

        updateLastModifiedTime(dirs);

    }
