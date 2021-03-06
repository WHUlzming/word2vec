        public void init(final IPageSite pageSite) {

            super.init(pageSite);

            final ActionRegistry registry = getActionRegistry();

            final IActionBars bars = pageSite.getActionBars();

            String id = ActionFactory.UNDO.getId();

            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.REDO.getId();

            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.DELETE.getId();

            bars.setGlobalActionHandler(id, registry.getAction(id));

        }
