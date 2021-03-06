    private void refreshFieldUrl(IBaseObject field, String url) {

        field.setAttribute(IDatafield.URL, url);

        if (field instanceof IModuleList) {

            IModuleList<IModule> list = (IModuleList) field;

            for (int i = 0; i < list.size(); i++) refreshFieldUrl(list.get(i), url + IDatafield.URL_DELIMITER + i);

        } else if (field instanceof IModule) {

            IModule entity = (IModule) field;

            for (String key : entity.keySet()) refreshFieldUrl(entity.get(key), url + IDatafield.URL_DELIMITER + key);

        }

    }
