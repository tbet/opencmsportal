<%@page buffer="none" session="false" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<cms:include file="../elements/header.jsp"/>

<div class="container">

    <!-- Main body -->
    <div class="row-fluid">
        <cms:container name="center" type="center" width="480" detailview="true"/>
    </div>

    <!-- footer -->
    <div class="row">
        <cms:container name="footer" type="center" width="480"/>
    </div>

    <hr>

    <footer>
        <p>&copy; eonas IT-Beratung und -Entwicklungs GmbH 2012</p>
    </footer>

</div>
<!-- /container -->
<cms:include file="../elements/footer.jsp"/>