    public RestServiceResult search(RestServiceResult serviceResult, Long nMaterialId) {

        CoMaterial coMaterial = new CoMaterialDAO().findById(nMaterialId);

        if (coMaterial == null) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("material.search.notFound"));

        } else {

            List<CoMaterial> list = new ArrayList<CoMaterial>();

            EntityManagerHelper.refresh(coMaterial);

            list.add(coMaterial);

            Object[] arrayParam = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.search.success"), arrayParam));

            serviceResult.setObjResult(list);

        }

        return serviceResult;

    }
