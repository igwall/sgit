#sgit project

#### Upgrades in progress : 
- [DONE] Init
-  [WIP] Parser => Always updated with new command :) 
-  [DONE ] Add
- ...



#### Resume :
This project is a clone of git, a code versioning tool (and more generally a file versioning tool) made during the month of October 2019. 

#### Installation :

- 1. Project setup : 
`git clone https://github.com/igwall/sgit.git`
`cd sgit/`

- 2. Use it with path:
> To complete

#### Documentation: 

##### init
Init is the command that create a new sgit project. It will create:
- `.sgit` folder with:
    - branches => Store all the branches (with the last commit of the current branch)
    - tags => Store all the tags (linked to a commit)
    - commit => All the commits
    - blobs => All the "hashed content" of your files linked to commit
    - HEAD (file) => The last commit hash
    - STAGE (file) => The files added with their current blob hash
    - .old => (folder used only in a business logic way)

##### Add
The add command allows you to add a file that will be tracked and commited. 
Use `sgit add /src/helloWorld.txt` to add this file in the tracked files list. 
> Some improvements are in progress like regular expression and multiples files adding. 

##### commit
This command allow you to create a commit. The content of commit is defined by all the files that you added before. 
Use `sgit commit -m "An awesome message"` to commit all your added files. 

##### status
This command show the differents "states" of files in your repository. It show to you all the files that were already added but edited in "`Edited files`" section. It shows you all the files added between your last commit and all your "current add" in `New files`. Of course, it allow you to show all the file that never been tracked in `untracked files`

