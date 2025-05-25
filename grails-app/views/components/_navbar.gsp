<div class="sticky-top" style="background-color: white;">
    <div class="row align-items-center">
        <div class="col-2 text-center">
            <g:link controller="home" action="index" style="text-decoration: none;"><div id="navBarLogo"><img id="applicationLogo" src='${assetPath(src: "applicationLogo.svg")}'/> Hipon.</div></g:link>
        </div>
        <div class="col">
            <div id="moduleName"></div>
        </div>
        <div class="col-2 text-end pr-5">
            <div class="row align-items-center">
                <div class="col d-none">
                    <img id="settingsIcon" src='${assetPath(src: "settings_icon.svg")}'/>
                </div>
                <div class="col d-none">
                    <img id="notifIcon" src='${assetPath(src: "notif_icon.svg")}'/>
                </div>
                <div class="col">
                    <div class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <img id="profileLogo" src='${assetPath(src: "profileLogo.svg")}'/>
                    </div>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownMenuLink" style="font-size: 25px">
                        <li>
                            <g:form controller="logout" method="POST">
                                <g:submitButton name="Logout" value="Logout" class="border-0"/>
                            </g:form>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>