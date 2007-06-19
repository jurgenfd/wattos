Essential settings for working under Eclipse:

Started as new Project from CVS without a .project being in the CVS repository.
then select:
New Project
	Web
	    Dynamic Web Project
    
Web Project Settings
	Context Root -> WebModule
	
	
Note for web apps to work; eclipse needs to have all libs in:
Wattos
    Java Resources
        Libraries
            Web App Libraries	
            
To get rid of the -red cross- thru the whole Wattos project in the explorer make sure:
Properties for Wattos
    Project Facets
        Project Facets
            DISABLE WebDoclet (XDoclet) 1.2.3            