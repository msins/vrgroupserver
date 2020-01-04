## VR-group server
- App used for managing surveys for different games, scenarios and questions through webapp. Any additions, deletions or updates are instantly synced with database. Newest information about any game can be obtained through rest api.
- Made with Vaadin, Hibernate, Jersey. Some other libraries used are Gson, Guava, Log4j 2.

## Testing
- Run `mvn jetty:run` within the vrgroupserver directory and open [http://localhost:8080](http://localhost:8080)

## Installation (for Ubuntu)
> `git clone https://github.com/msins/vrgroupserver.git <dir>`

> `bash <dir>/vrgroupserver/setup/setup.sh <dir>/vrgroupserver/`

## Current rest API v1
- returns JSON-formatted data.
- base url `http://<ip>/api/`
#### Implementations
- `GET /v1/games/<game_name>` fetches all the relevant data for a game
```json
{
  "scenarios": [
    {
      "scenario": {
        "id": 2,
        "name": "Default"
      },
      "questions": [
        {
          "id": 3,
          "text": "Question text 1",
          "choices": [
            {
              "id": 7,
              "value": "Choice 1"
            },
            {
              "id": 8,
              "value": "Choice 2"
            }
          ],
          "type": "MULTIPLE_CHOICE"
        },
        {
          "id": 4,
          "text": "Question text 2",
          "choices": [
            {
              "id": 9,
              "value": "Choice text 1"
            },
            {
              "id": 10,
              "value": "Choice text 2"
            }
          ],
          "type": "SCALING_QUESTION"
        }
      ]
    }
  ]
}
```
- `POST /v1/games/<game_name>`

