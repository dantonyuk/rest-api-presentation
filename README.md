# REST API example

It's a simple vote application providing REST API that manages users, polls and votes.

### Run the Application

To run the application please execute
```bash
./gradlew bootRun
```

### API

You can follow `http://localhost:8080/api` to get all the information
about API. The application also provides [the browser](http://localhost:8080/api)
which displays all the required data.

The API is following REST API naming best practices.
Collection URIs are:

* /api/users
* /api/polls
* /api/votes
* /api/votes/:voteId/options

Supported methods are GET/POST for collections and GET/PUT/PATCH/DELETE for entities.

### Additional Materials

* [Postman Collection](doc/REST-Example.postman_collection.json)
* [PowerPoint Presentation](doc/RESTful-API.pptx)
