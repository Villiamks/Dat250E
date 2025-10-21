# Dat250 Expass4

### Execution:
To start created and copied the given test method into my project.
I then edited the User class, adding the methods shown and changing
the name to Users because User is reserved in SQL. To do this I did
change the provided code slightly by changing the table name to users.

### Problems: 
As I said above I needed to change the User class, but this was not 
the only one. Every class I had needed small name changes and the
long id got changed to int id. This had effects on my PollManager
as some of the functions no longer worked as intended, this was all
changed so that they worked again.

I added the @ Annotations to the classes, and had some problems 
getting the correct ones, but in the end everything worked. The 
tests provided do pass alongside my previous tests.

The frontend had a lot of problems doe to the changes in the API.
Mostly it was just name changes, but also some relational loops 
where Users have a collection of created Polls, and each Poll 
has a creator. This was solved by the @JsonIgnore annotation.

The code to the Frontend can still be found at: https://github.com/Villiamks/Dat250EFront.git 

### Not finished: 
There is still a problem in the frontend that I seemingly cant 
resolve. It has problems showing the voteOptions in the display.
it is supposed to be shown in an "each" loop like:

{#each poll.options as option}

But even when the {console.log(poll.options[0])} has the correct
value and all needed information and was written in the same 
part of the code, it still does not work.