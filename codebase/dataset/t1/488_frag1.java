            try {

                FileInputStream in = new FileInputStream(files[i]);

                ZipEntry entry = new ZipEntry(files[i].getName());

                zipFile.putNextEntry(entry);

                int c;

                while ((c = in.read()) != -1) zipFile.write(c);

                zipFile.closeEntry();

                in.close();

            } catch (FileNotFoundException fnfe) {
