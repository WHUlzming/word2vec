    public static void main(String[] args) {

        try {

            DocumentPublish object = DocumentPublish.getInstance("000000070201802", 29002);

            object.rePublish();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
