            out.write("                };\t\t\t\r\n");

            out.write("                map = new OpenLayers.Map('map', options);\r\n");

            out.write("                // setup tiled layer\r\n");

            out.write("                var tiled = new OpenLayers.Layer.WMS(\r\n");

            out.write("                \"cite:ktm_roads01 - Tiled\", \"http://localhost:8080/geoserver/cite/wms\",\r\n");

            out.write("                {\r\n");

            out.write("                    LAYERS: 'cite:ktm_roads01',\r\n");

            out.write("                    STYLES: '',\r\n");

            out.write("                    format: format,\r\n");

            out.write("                    tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom\r\n");

            out.write("                },\r\n");
