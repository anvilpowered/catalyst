# Docker Compose Example

To run this example, you need to have [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed.

Then, run use `docker-compose up` to start the containers and `docker-compose down` to stop them.

You will not be able to join the server on their first boot,
as the config files must be patched and do not exist at that point yet.
To fix this, restart the containers with the `docker-compose restart`.

The docker-compose configuration mounts the server data directories to `./paper-0` and `./paper-1`, respectively.
The proxy data volume is not mounted.
