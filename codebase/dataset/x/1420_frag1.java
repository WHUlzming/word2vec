        } else if (vElementName.equalsIgnoreCase("COLOR_MIN")) {

            this.beerXml.getRecipe().getStyle().setColor_Min(Util.str2dbl(vStringElementValue));

        } else if (vElementName.equalsIgnoreCase("COLOR_MAX")) {

            this.beerXml.getRecipe().getStyle().setColor_Max(Util.str2dbl(vStringElementValue));

        } else if (vElementName.equalsIgnoreCase("CARB_MIN")) {

            this.beerXml.getRecipe().getStyle().setCarb_Min(Util.str2dbl(vStringElementValue));

        } else if (vElementName.equalsIgnoreCase("CARB_MAX")) {
