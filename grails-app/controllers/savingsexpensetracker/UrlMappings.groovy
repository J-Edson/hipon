package savingsexpensetracker

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(redirect: [uri: '/home/index'])
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
