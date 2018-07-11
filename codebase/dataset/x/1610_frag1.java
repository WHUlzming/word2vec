    public ModelAndView deleteManyItems(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        Enumeration enumeration = request.getParameterNames();

        List itemsToDelete = new ArrayList();

        while (enumeration.hasMoreElements()) {

            String key = (String) enumeration.nextElement();

            if (key.startsWith("delete")) {

                itemsToDelete.add(channelController.getItem(Long.valueOf(key.substring(6))));

            }

        }

        channelController.deleteItems(itemsToDelete);

        return new ModelAndView("", "message", "items.deleted");

    }
