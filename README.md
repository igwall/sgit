# sgit project

👨‍💻 
| - http://igwall.fr
| - https://www.linkedin.com/in/igwall/

#### Resume :
This project is a clone like of git, a code versioning tool (and more generally a file versioning tool) made during the month of October 2019. 


#### Upgrades in progress : 
- ✅ Init
- ✅ Parser 
- ✅ Add
- ✅ Commit
- ✅ Status
- ✅ Diff
- ✅ Log
- ✅ Help
- 🕓 Checkout

### Installation (manual) :
> This Scala project use the `sbt assembly` so please make sure that you've installed `sbt` before using it. The scala version used is  **2.13.0**


1. `git clone https://github.com/igwall/sgit.git`
2. `cd sgit/`
3. `sbt assembly`
4. Open your .bashrc or .zshrc and add this content at the end of file (change "/My/Path/ with a correct one): 
```bash
    export PATH="/My/Path/sgit/target/scala-2.13/:$PATH"
    alias sgit="sgit-1.0"
```
5. Refresh your bash file : `source .zshrc` or `source .bashrc`
6. Enjoy the wonderfull (but incomplete) **sgit** command 💪🏻



### Documentation: 

#### init
Init is the command that create a new sgit project. It will create:
- `.sgit` folder with:
    - branches => Store all the branches (with the last commit of the current branch)
    - tags => Store all the tags (linked to a commit)
    - commit => All the commits
    - blobs => All the "hashed content" of your files linked to commit
    - HEAD (file) => The last commit hash
    - STAGE (file) => The files added with their current blob hash
    - .old => (folder used only in a business logic way)

#### add
The add command allows you to add a file that will be tracked and commited. 
Use `sgit add /src/helloWorld.txt` to add this file in the tracked files list. 
> Some improvements are in progress like regular expression and multiples files adding. They are not available right now.

#### commit
This command allow you to create a commit. The content of commit is defined by all the files that you added before. 
Use `sgit commit -m "An awesome message"` to commit all your added files. 

#### status
This command show the differents "states" of files in your repository. It show to you all the files that were already added but edited in "`Edited files`" section. It shows you all the files added between your last commit and all your "current add" in `New files`. Of course, it allow you to show all the file that never been tracked in `untracked files`

#### diff
This command show the differents between your current stage status (all the files added) and your current working directory. 

#### branch
This command allow you to create branches : 
- `sgit branch myBranchName` will create a new branch. 
- `sgit branch -av` show you all the branches

#### log
This command display the history of commit (hash and messages). 
