    public static UpdateResult update_minutiae(MINUTIAE minutiae, MINUTIA minutia, boolean[] bdata, final int iw, final int ih, final LFSPARMS lfsparms) {

        final int qtr_ndirs = lfsparms.num_directions / 4;

        final int full_ndirs = lfsparms.num_directions * 2;

        if (minutiae.getNum() > 0) {

            for (int i = 0; i < minutiae.getNum(); i++) {

                final int dx = Math.abs(minutiae.get(i).y - minutia.y);

                if (dx < lfsparms.max_minutia_delta) {

                    final int dy = Math.abs(minutiae.get(i).y - minutia.y);

                    if (dy < lfsparms.max_minutia_delta) {

                        if (minutiae.get(i).type == minutia.type) {

                            int delta_dir = Math.abs(minutiae.get(i).direction - minutia.direction);

                            delta_dir = Math.min(delta_dir, full_ndirs - delta_dir);

                            if (delta_dir <= qtr_ndirs) {

                                if ((dx == 0) && (dy == 0)) {

                                    return (UpdateResult.UPDATE_IGNORE);

                                }

                                boolean contourSearchResult = Contour.search_contour(minutia.x, minutia.y, lfsparms.max_minutia_delta, minutiae.get(i).x, minutiae.get(i).y, minutiae.get(i).ex, minutiae.get(i).ey, RotationalScanDirection.SCAN_CLOCKWISE, bdata, iw, ih);

                                if (contourSearchResult) {

                                    return UpdateResult.UPDATE_IGNORE;

                                }

                                if (Contour.search_contour(minutia.x, minutia.y, lfsparms.max_minutia_delta, minutiae.get(i).x, minutiae.get(i).y, minutiae.get(i).ex, minutiae.get(i).ey, RotationalScanDirection.SCAN_COUNTERCLOCKWISE, bdata, iw, ih)) {

                                    return (UpdateResult.UPDATE_IGNORE);

                                }

                            }

                        }

                    }

                }

            }

        }

        minutiae.add(minutia);

        return UpdateResult.UPDATE_SUCCESS;

    }
