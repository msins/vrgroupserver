### About
- App used for managing surveys for different games, scenarios and questions through webapp. Any additions, deletions or updates are instantly synced with database. Newsest information about any game can be obtained through rest api.
- Made with Vaadin, Hibernate, Jersey. Some other libraries used are Gson, Guava, Log4j 2.

> Test locally with `mvn jetty:run` on [http://localhost:8080](http://localhost:8080)

Setup instructions (for Ubuntu):
> `git clone https://github.com/msins/vrgroupserver.git <dir>`

> `bash <dir>/vrgroupserver/setup/setup.sh <dir>/vrgroupserver/`

Current rest api:
> `<ip>/api/v1/games/<game_name>`

