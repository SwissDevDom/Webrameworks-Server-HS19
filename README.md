# Webrameworks-Server-HS19
This is an educational project to learn the basics of building a server that handles requests from a webclient, stores information in a MongoDB database and sends said information from the DB back to the client.  
</br>
There are 3 versions (each one has it's own directory) of this server, which all serve the same purpose, but are built with different technologies:
### "flashcard-basic"
* SpringMVC with Thymeleaf Templating for the frontend rendering, no webclient needed

### "flashcard-spring" & "flashcard-rest"
* Pure SpringMVC with REST-Calls to the MongoDB database. Sends information from the database to the client on request

### "flashcard-express"
* Node/Express server with REST-Calls to the MongoDB database. Sends information from the database to the client on request
