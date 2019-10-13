#sgit project

#### Upgrades in progress : 
- ✅ Init
- 🕓 Parser => Always updated with new command :) 
- ✅ Add
- ✅ Commit
- ✅ Status
- 🕓 Diff
- ...



#### Resume :
This project is a clone of git, a code versioning tool (and more generally a file versioning tool) made during the month of October 2019. 

#### Installation (manual) :
> THis Scala project use Assembly so please make sure that you've installed `sbt` before using it.

- 1. Project setup : 

1. `git clone https://github.com/igwall/sgit.git`
2. `cd sgit/`
3. `sbt assembly`
4. Open your .bashrc or .zshrc and add this content at the end of file : 
```bash
    export PATH="/Users/lucasgoncalves/Git/sgit/target/scala-2.13/:$PATH"
    alias sgit="sgit-0.1.0-SNAPSHOT"
```
5. Refresh your bash file : `source .zshrc` or `source .bashrc`
6. Enjoy the wonderfull **sgit** command 💪🏻


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

