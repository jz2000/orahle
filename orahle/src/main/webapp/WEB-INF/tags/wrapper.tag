<%@tag description="Simple Wrapping Tag" pageEncoding="UTF-8"%>
<%@attribute name="title" fragment="false" required="true" %>
<%@attribute name="caption" fragment="false" required="true" %>
<%@attribute name="crumbs" fragment="true" required="true" %>
<html>
    <head><title>Orahle: ${title}</title></head>
    <body>
        <div id="header" style="font-size: 6px;color: green">
            orahle header
        </div>
        <div id="crumbs">
            <jsp:invoke fragment="crumbs"/>
        </div>
        <div id="caption">
            <h1>${caption}</h2>
        </div>
        <div id="body">
        <jsp:doBody/>
        </div>
        <div id="footer" style="font-size: 6px;color: magenta">
            orahle footer
        </div>
    </body>
</html>