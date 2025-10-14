# Dat250 Expass2:

### Execution:
I started With the code from Expass1 and added the classes 
represented in the Domain Model provided. Then I wrote the 
PollManager class for managing these classes.

### Problems:
I ran into problems when setting up the controller. I am not
very used to SpringBoot and I therefore had some problems with 
the annotations used (@GetMapping...) and when to use them.

I had the same problems writing the tests, but as I have used 
junit before I defaulted to this. 

Another problem I ran into was that the objects stored in the 
hashmap needed unique IDs. First I implemented it so that it 
sett the size of the list + 1 as an id, but this will not work 
when one can delete or remove things from the hashmaps. To solve
this I checked stackoverflow and asked AI for help, both seemed
to recommend AtomicLong for incrementing IDs. This worked very 
well.