        assertTrue(comment1.isApproved());

        assertEquals(sdf.parse("05 Apr 2004 23:27:30:0 +0100"), comment1.getDate());

        assertFalse(comment1.isAuthenticated());

        Comment comment2 = (Comment) comments.get(1);

        assertEquals("Re: " + blogEntry.getTitle(), comment2.getTitle());
