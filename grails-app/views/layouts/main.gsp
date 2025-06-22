<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Savings Expense Log"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>

    <!-- Bootstrap CSS-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <!-- Google Fonts-->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Archivo+Black&family=Dongle&family=Inter:wght@100..900&family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&display=swap" rel="stylesheet">
    <!-- Google Chart-->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <!-- Swiper JS-->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
    <!-- sweetalert2-->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <g:layoutHead/>
</head>

<body>
    <%
    def currentUri = request.forwardURI
    def appName = grailsApplication.config.getProperty('info.app.name')
    %>
    <g:if test="${currentUri == '/'+appName+'/login/auth'}">
        <div class="d-none d-lg-block">
            <g:layoutBody/>
        </div>
    </g:if>
    <g:else>
        <div class="d-none d-lg-block">
            <g:render template="/components/navbar"/>
            <div class="row" >
                <div class="col-2">
                    <g:render template="/components/sidebar"/>
                </div>
                <div class="col">
                    <g:layoutBody/>
                </div>
            </div>
        </div>
    </g:else>
    <asset:javascript src="application.js"/>
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>    
</body>
</html>
