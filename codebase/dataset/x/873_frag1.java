            out.write("\t\t\t\t}\r\n");

            out.write("\t\t\t\treturn;\r\n");

            out.write("\t\t\t}\r\n");

            out.write("\t\t}\r\n");

            out.write("\t}\r\n");

            out.write("\tif(level==2 && !$$(\"menu\"+menuId).checked){\r\n");

            out.write("\t\tfor(var i=0;i<menuArray2.length;i++){\r\n");

            out.write("\t\t\tfor(var j=0;j<menuArray2[i].length;j++){\r\n");

            out.write("\t\t\t\tif(menuArray2[i][j].menuId==menuId){\r\n");

            out.write("\t\t\t\t\tfor(var k=0;k<menuArray3[i][j].length;k++){\r\n");

            out.write("\t\t\t\t\t\t$$(\"menu\"+menuArray3[i][j][k].menuId).checked=false;\r\n");
