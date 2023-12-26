# Docker Compose Example

To run this example, you need to have [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed.

Once installed, use `docker-compose up` to start the containers and `docker-compose down` to stop them.

You will not be able to join the server on its first boot,
as the config files must be patched and do not exist at that point yet.
To fix this, restart the containers with the `docker-compose restart`.

The docker-compose configuration mounts the server data directories to `./paper-0` and `./paper-1`, respectively.
The proxy data volume is not mounted.

## Instructions

- Run `./gradlew shadowJar` in the root directory of the project.
  This will create a build artifact in `./build/libs` which is copied to the proxy plugins directory automatically.
- Run `cd examples/docker-compose` to change into this directory.
- Run `docker-compose up --build` to build and start the containers.

## Notes

Because of the way containers work, it is not possible to type into the minecraft server console.
To work around this, the minecraft-server image comes with built in RCON support.
To run a command on one of the paper servers, attach a terminal and use the `rcon-cli` command.

However, the proxy image does not come with RCON support and the RCON plugin doesn't work.
To work around this, there is a standalone luckperms container configured in the `docker-compose.yaml`
which is connected to the PostgreSQL database.

To set permissions, attach a terminal to the standalone luckperms container and run the command:
`send lp user <your-name> p set 'luckperms.*'`

The quotes are important to prevent your shell from preemptively expanding the asterisk.
