# Dat250 Expass7

### Execution: 
To accomplish this task I first tried to use the base images provided
by gradle, but I could not seem to get them to work. Because of this 
I tried adapting the dockerfile from lecture 15, but I could not seem
to get it to work either. I realized that the problem came from the
Valkey dependencies. To fix this I tried setting up a docker-compose.yml 
file and building with the docker compose command, but again I had no
success. In the end the solution I went for, tough not perfect, is to
run the valkey container and poll-app container separately, this does
work, but is obviously not optimal.