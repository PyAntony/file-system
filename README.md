# A Small File System with Scala

Application to simulate a file system and commands (analogous to Linux commands). Codebase was provided 
by Daniel Ciocîrlan in his Udemy course "Rock the JVM! Scala and Functional Programming for Beginners" 
(I highly recommend Daniel's courses for beginners and advanced Scala programmers. For more information 
on his courses go to https://rockthejvm.com/).

I have made multiple modifications to the original codebase to make the code more succinct, readable, and functional. 
For example: using pattern matching rather than nested IF statements, Option monads replace null values, using utility 
methods in the Command object, etc. I have also added 1 additional command: CP.

## Scaffold description 

Scaffold remains identical to the original. Packages:

**- system:** contains the Filesystem app and the State class. State changes from one command to the next; it holds the
root directory, the current working directory, and any message to be displayed in the terminal. The app runs with a
recursive function that displays the state message, waits for a new input, and updates the state 
according to the input provided:

```scala
def systemIteration(state: State = state, scanner: Scanner = scanner): Unit = {

    state.show()
    systemIteration(Command.from(scanner.nextLine()).apply(state))
  }

  systemIteration()
```

**- files:** 2 types of entries (which inherit from the abstract class DirEntry) are supported: Directories and Files. 
Directories can hold additional directories.

**- commands:** each command is represented by its own class. they extend the Command trait with the following contract:
 
```scala
trait Command extends (State => State)
```

So a command's instance apply function receives a state and returns a state. New commands are inserted in the Command 
companion object:

```scala
def from(input: String): Command = {
    val tokens: List[String] = input.split(" ").toList
    val cmd = if (tokens.nonEmpty) tokens.head else "no command"
    val incomplete = tokens.size < 2
    
    cmd match {
      case "no command" => emptyCommand
      case MKDIR => if (incomplete) incompleteCommand(MKDIR) else new Mkdir(tokens(1))
      case TOUCH => if (incomplete) incompleteCommand(TOUCH) else new Touch(tokens(1))
      case CD => if (incomplete) incompleteCommand(CD) else new Cd(tokens(1))
      case RM => if (incomplete) incompleteCommand(RM) else new Rm(tokens(1))
      case CAT => if (incomplete) incompleteCommand(CAT) else new Cat(tokens(1))
      case ECHO => new Echo(tokens.tail)
      case PWD => new Pwd
      case LS => new Ls
      case CP => new Cp(tokens.tail)
      case _ => new UnknownCommand
    }
}
```

### Demo

<kbd>![demo](https://github.com/PyAntony/file-system/blob/master/images/demo.png)</kbd>

