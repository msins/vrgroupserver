## VR-group university project
- Webapp used for managing surveys for different games, scenarios and questions through web interface. Any additions, deletions or updates are instantly synced with database. Newest information about any game can be obtained through the rest API.
- Made with Vaadin, Hibernate, Jersey. Running on Jetty. Some other libraries used are OpenCSV, Gson, Guava, Log4j 2.
- Didn't use MVP since ui is pretty simple, probably should have

## Testing
- Run `mvn jetty:run` within the vrgroupserver directory and open [http://localhost:8080](http://localhost:8080)
(make sure to change the port, from 80 to 8080 in pom.xml line 248, or run with sudo on [http://localhost](http://localhost))
## Installation (for Ubuntu)
```
git clone https://github.com/msins/vrgroupserver.git <dir>
bash <dir>/vrgroupserver/setup/setup.sh <dir>/vrgroupserver/
```

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
              "value": "Choice text 1"
            },
            {
              "id": 8,
              "value": "Choice text 2"
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
            },
            {
              "id": 11,
              "value": "Choice text 3"
            }
          ],
          "type": "SCALING"
        }
      ]
    }
  ]
}
```
- `POST /v1/games/<game_name>` saves answer for a given user (project required basic user information)
- this could(should) have been implemented to receive json containing only ids and not other redundant information but it was easier for other groups in project to use this format
```json
{
  "choice":{
    "id":7,
    "value":"Choice text 1"
  },
  "question":{
    "choices":[
      {
        "id":7,
        "value":"Choice text 1"
      },
      {
        "id":8,
        "value":"Choice text 2"
      }
    ],
    "id":3,
    "text":"Question text 1",
    "type":"MULTIPLE_CHOICE"
  },
  "scenario":{
    "id":2,
    "name":"Default"
  },
  "user":{
    "age":21,
    "email":"email@domain.com",
    "gender":"M",
    "name":"name"
  }
}
```
- Csv can be fetched with `GET /v1/csv/<game_name>`

