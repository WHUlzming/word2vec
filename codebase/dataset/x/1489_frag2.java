    public void HtmlFromFile(String path) {

        File file = new File(path);

        if (!file.exists()) {

            System.out.println("�ļ������ڣ�");

            return;

        }

        BufferedReader reader = null;

        StringBuffer content = null;

        try {

            content = new StringBuffer();

            reader = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = reader.readLine()) != null) content.append(line + "\r\n");

            setOriHtml(content.toString());

            reader.close();

            reader = null;

            this.isFromWeb = false;

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (reader != null) {

                reader = null;

            }

        }

    }
