    public static List<File> getFileList(Task task, String items) {

        ArrayList<File> result = new ArrayList<File>();

        StringTokenizer tk = new StringTokenizer(items, ":");

        while (tk.hasMoreTokens()) try {

            String item = tk.nextToken();

            File file = new File(item);

            if (!file.isAbsolute()) file = new File(task.getProject().getBaseDir(), item).getAbsoluteFile();

            result.add(file.getCanonicalFile());

        } catch (IOException ex) {

        }

        return result;

    }
