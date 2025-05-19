# office-space-manager

This application is developed for Ålesund Kommune by students at NTNU in Ålesund.

## Prerequisites
To run the application with dev mode off you will need to register a Microsoft Entra id app. See how to create one [here](https://learn.microsoft.com/en-us/entra/identity-platform/quickstart-register-app).

You will also need a database SQL database provider. We recommend using PostgreSQL since that is what the project was developed with, however any other should work, as long as they have a JDBC driver. In this case, you will need to change the settings in `application.properties`

NPM and node is required for the front end.

### .env file
The .env contains information that is needed for the backend to run. The file should be located on the top level of the chairspace project.

The structure of the .env file is as follows
```.env
FRONTEND_URL=http://localhost:3000 # WIll be the frontend url with the current setup can be changed later
DATABASE_PASSWORD=[DATABASE_PASSWORD]
DATABASE_USERNAME=[DATABASE_USERNAME]
DATABASE_URL=[YOUR_URL]

AZURE_AD_TENANT_ID=[YOUR_SECRET_HERE]
AZURE_AD_CLIENT_ID=[YOUR_SECRET_HERE]
AZURE_AD_CLIENT_SECRET=[YOUR_SECRET_HERE]

# If DEV_MODE is on, the security pipeline will not be active.
# With no session user a lot of features will break, but this is useful to be able to populate the database
DEV_MODE=false
```
## How to run locally
The two parts in the application are in separate folders. To start the front end, navigate to the front-end folder, and run the `npm run dev` command.

To run the back end, first navigate to the back-end folder, then to the chairspace folder.
Here you need to run this command ``mvn spring-boot:start` This should start backend application given a .env file exists with the correct information.
The first user that logs on to the application will become an application manager.

It should also be possible to use the RUN_dev.bat script.

## How to populate the database

A script exists to help populate the database as there currently is no way to do this from the UI. To run this script you first need to get the backend to run (See how to run locally). When this is done turn on DEV_MODE in the .env file. When this is done you can use the `populator.ipynb` to populate the database with both faux users, areas, and reservations

Note that using the script under the area header will create random areas, while using the script under the building header will create a Building-Floor-Room structure.

## Configure the Front-end
To configure the front-end you need to change the `front-end/src/plugins/config.js` file. This file contains the configuration for the front end. This file defines the url to the api, in addition it also defines the icons for area types and features. It is possible to map all [Material Design icons](https://fonts.google.com/icons) to a feature