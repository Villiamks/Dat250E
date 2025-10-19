# Dat250 Expass3

### Execution:
I mostly followed the given step-by-step instructions, but I
will admit that I did step 2 and 4 at the same time and step
3 after. This is because I misunderstood the text and thought
I was supposed to implement the API functions in the frontend
in step 2.

I also created a login and registration system for creating 
and logging inn as a user. This was made using the localstorage
functions in javascript. I did this because a poll cannot be
created by someone how is not logged inn, and the need for 
a poll to have a creator. 

I am aware that it is better to create components and call 
these in a main file instead of writing all the code inn
one file. However, due to the time restraint and inexperience
with this framework, almost all the code was written in one 
.svelte file not including the Registration component (NewUser).

code can be found at this link: https://github.com/Villiamks/Dat250EFront.git

### Not finished:
I was not able to show the number of votes on each voteOption
on a poll. This is because the vote() function does not update
the List of votes in a voteoption, it only updates the list of
votes on each poll. It is information that is available, but 
I hope there will be a better way of doing this through the 
next experiments where we add a database.   

I also did not do the optional tasks.