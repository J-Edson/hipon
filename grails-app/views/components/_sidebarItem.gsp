  <li class="nav-item" >
    <div class="sidebar-items" id="${sidebarItem}-item">
      <div class="row align-items-center p-3">
        <div class="col text-right">
          <% 
            def srcPath =  sidebarItem +"_icon.svg" 
            def srcPath1 =  sidebarItem +"_icon1.svg"
          %>
          <img id="${sidebarItem}Icon" src='${assetPath(src: srcPath)}' class="sidebar_icon"/>
        </div>
        <div class="col text-left">
          <div id="${sidebarItem}Sub">${sidebarItem}</div>
        </div> 
      </div>
    </div>
  </li>

<script>
    let ${sidebarItem}Item = document.getElementById(`${sidebarItem}-item`);
    let ${sidebarItem}Icon = document.getElementById(`${sidebarItem}Icon`);
    let ${sidebarItem}Sub = document.getElementById(`${sidebarItem}Sub`);

    //if(`${sidebarItem}`.toLowerCase() != window.location.pathname.split("/").filter(Boolean)[0].toLowerCase()){ //for localhost
    if(`${sidebarItem}`.toLowerCase() == window.location.pathname.split("/").filter(Boolean)[1].toLowerCase()){ // for cloud
      ${sidebarItem}Icon.src=`${assetPath(src: srcPath1)}`
      ${sidebarItem}Sub.style.color='#2D60FF'
      ${sidebarItem}Item.style.borderLeft='5px solid #2D60FF';

      document.getElementById(`moduleName`).innerHTML = `${sidebarItem}`;
      console.log(`${sidebarItem}`)
    }

    ${sidebarItem}Item.addEventListener('mouseover', function() {
        ${sidebarItem}Icon.src=`${assetPath(src: srcPath1)}`
        ${sidebarItem}Sub.style.color='#2D60FF'
        ${sidebarItem}Item.style.borderLeft='5px solid #2D60FF';
    });
    ${sidebarItem}Item.addEventListener('mouseout', function() {
        //if(`${sidebarItem}`.toLowerCase() != window.location.pathname.split("/").filter(Boolean)[0].toLowerCase()){ //for localhost
        if(`${sidebarItem}`.toLowerCase() != window.location.pathname.split("/").filter(Boolean)[1].toLowerCase()){ // for cloud
          ${sidebarItem}Icon.src=`${assetPath(src: srcPath)}`
          ${sidebarItem}Sub.style.color='#B1B1B1'
          ${sidebarItem}Item.style.border='none';
        }
    });

    ${sidebarItem}Item.addEventListener('click', function() {
      //let path = `${sidebarItem}`.toLowerCase()
      //window.location.href = '/'+path+'/index';
      window.location.href = `${sidebarPath}`;
    });
</script>