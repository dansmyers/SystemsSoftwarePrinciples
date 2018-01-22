# Regular Expressions

This short lab will let you practice using regular expressions with `grep`, a standard Unix program for searching files.

## Repository

First, log into Cloud 9 and create a new CMS330 workspace for this course. The name of your workspace should be **CMS330** and the 
description should be **CMS 330 Your Name**, where you fill in your name. Choose C/C++ for the workspace type.

As soon as the workspace becomes available, go to the **Sharing** tab in the upper right and give me (dmyers) access to your workspace.

## When Trouble Comes Along, You Must `grep` It

`grep` is short for *globally search a regular expression and print*. It's basic use case takes two arguments:

    - A regular expression
    
    - A file name
    
`grep` searches the target file for any strings matching the pattern described by the regular expression and prints them to the screen.

### Wordlist

There is a tradition of including a plain text dictionary of words a part of a Unix installation, for use with spellchecking and password
programs. The word list we used for the password cracking program was a medium sized example.

Install a wordlist into your Cloud 9 repo using:

```
prompt$ sudo apt-get install wamerican-large
```

This command will install a large list of patriotic American words into the directory `/usr/share/dict`. Other less patriotic wordlists
are also available. You can run `sudo apt-get install wordlist` to see a list of all the options.

### Searching for a substring

The simplest `grep` case is to search target file for a specified substring:

```
prompt$ grep cat /usr/share/dict/american-english-large
```

The second argument is the complete path to the dictionary file. Remember that Unix file paths always begin at the root directory, which
is denoted by `/`. Pro tip: press TAB to autocomplete the part of the path that you're currently typing.

The command prints all words in the file containing the string `cat`.
