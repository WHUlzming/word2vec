        } else if (parent == null) {

            throw new SystemException(ErrorCodes.INTERNAL_ERROR, parentSrc + " is not parent for " + src);

        } else if (parentSrc.equals(parent)) {

            return src.getName();

        }

        String path = getRelativePathFrom(parentSrc, parent);
