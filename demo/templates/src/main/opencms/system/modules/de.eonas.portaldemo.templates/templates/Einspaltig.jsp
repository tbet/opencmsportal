<%@page buffer="none" session="false" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<cms:include file="../elements/header.jsp"/>

<div class="container">
    <div class="row">
        <div class="span3">
            <cms:include file="../elements/menu/nav_side.jsp"/>

            <cms:container name="left" type="side" width="160"/>
        </div>

        <div class="span9">
            <cms:container name="center" type="center" width="480" detailview="true"/>
        </div>
    </div>

    <div class="row">
        <div class="span12">
            <cms:container name="footer" type="center" width="480"/>
        </div>
    </div>

    <hr>

    <footer>
        <p>&copy; eonas IT-Beratung und -Entwicklungs GmbH 2012</p>
    </footer>

</div>

<cms:include file="../elements/footer.jsp"/>