    private boolean _jspx_meth_c_005fif_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f4, PageContext _jspx_page_context) throws Throwable {

        PageContext pageContext = _jspx_page_context;

        JspWriter out = _jspx_page_context.getOut();

        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);

        _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);

        _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f4);

        _jspx_th_c_005fif_005f12.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${task eq 'create'}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());

        int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();

        if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {

            do {

                out.write("\r\n");

                out.write("\t<script type=\"text/javascript\">\r\n");

                out.write("\tnew LiveValidation('password').add(Validate.Presence);\r\n");

                out.write("\tnew LiveValidation('password2').add(Validate.Presence);\r\n");

                out.write("</script>\r\n");

                int evalDoAfterBody = _jspx_th_c_005fif_005f12.doAfterBody();

                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;

            } while (true);

        }

        if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {

            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);

            return true;

        }

        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);

        return false;

    }
