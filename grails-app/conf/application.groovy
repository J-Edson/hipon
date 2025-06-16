

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'user.Person'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'user.PersonAuthority'
grails.plugin.springsecurity.authority.className = 'user.Authority'
grails.plugin.springsecurity.useSessionFixationPrevention = true
grails.plugin.springsecurity.logout.invalidateHttpSession = true
grails.plugin.springsecurity.sessionFixationPrevention.alwaysCreateSession = true
grails.plugin.springsecurity.logout.clearAuthentication = true
grails.plugin.springsecurity.logout.redirectUrl = '/login/auth'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]


