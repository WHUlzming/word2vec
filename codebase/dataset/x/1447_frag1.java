        while (i.hasNext()) {

            CmsResource sibling = (CmsResource) i.next();

            links.add(sibling.getRootPath());

            System.out.println("Sibling: " + sibling.toString());

        }

        assertEquals(2, links.size());
