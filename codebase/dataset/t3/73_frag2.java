    private boolean _jspx_meth_c_005fif_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);

        _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);

        _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);

        _jspx_th_c_005fif_005f2.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.departmentId!=department.departmentId }", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());

        int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();

        if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            do {

                out.write("\r\n");

                out.write("\t\t\t\t\t\t\t\t\t\t<option value=\"");

                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${department.departmentId}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));

                out.write('"');

                out.write(' ');

                out.write('>');

                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${department.departmentName}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));

                out.write("</option>\r\n");

                out.write("\t\t\t\t\t\t\t\t\t");

                int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

        }

        if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);

            return true;

        }

        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);

        return false;

    }
