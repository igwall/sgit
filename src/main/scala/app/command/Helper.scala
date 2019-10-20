package app.command

object Helper {
  def getHelp(): String = {
    s"""
Start a working area :
${Console.YELLOW}init${Console.RESET}       Create an empty Sgit directory
            > sgit init

Work on the current change :   
${Console.YELLOW}add${Console.RESET}         Add file contents to the STAGE file 
            > sgit add /myFile.txt

Examine the history and state :
${Console.YELLOW}status${Console.RESET}      Show the working tree status
            > sgit status

Grow, mark and tweak your common history
${Console.YELLOW}branch${Console.RESET}      Create a new branch 
            > sgit branch myBranch
${Console.YELLOW}branch -av${Console.RESET}  List all branches 
            > sgit branch -av\n 

${Console.YELLOW}tag${Console.RESET}         Create a new tag 
            > sgit tag newTag
${Console.YELLOW}tag -av${Console.RESET}     List all tags
            > sgit tag -av 

${Console.YELLOW}commit${Console.RESET}      Record changes to the repository 
            > sgit commit
${Console.YELLOW}commit -m${Console.RESET}   Record changes with a specific message 
            > sgit commit -m "my commit message"

${Console.YELLOW}diff${Console.RESET}        Show changes between commits, commit and working tree, etc
            > sgit diff
            
"""
  }
}
