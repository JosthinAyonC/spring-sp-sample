# BaseProyectoSpringboot
## Docker 
- Run project  
```
docker-compose up
```
Stop project
```
docker-compose down
```

## Swagger url
/api/v2/swagger-ui/index.html

## Endpoints

### USER

**GET /api/v2/user**
- Returns a list of all users.

**GET /api/v2/user/{id}**
- Returns a user by their ID.

**POST /api/v2/user**
- Create a system user.
- Request Body Format:

### Create Task Request


```json
  {
      "username": "userexample",
      "email": "user@example.com",
      "password": "password#21",
      "firstname": "User",
      "lastname": "Example",
      "img": ""
  }
```

**POST /api/v2/user/mod**

- Create a user system with the role assigned by a moderator.
- Request Body Format:
  ```json
    { 
			 "username": "userexample",
			 "dni": "09543895954",
			 "email": "user@example.com",
			 "password": "password#21",
			 "firstname": "User",
			 "lastname": "Example",
			 "img": "",
			 "roleId": Long
    }
  ```

- You can assign a role to the created user.

**PUT /api/v2/user/edit/{id}**

- Edit a user system.
- You cannot edit the roles or password (there is an endpoint for that).
- Request Body Format:
   
    
- { "": ""}
- Comment: It is a partial edit; only send the field that you want to edit.

**PUT /api/v2/user/delete/{id}**

- A void endpoint that performs logical user deletion by ID.

**PUT /api/v2/user/changepass/{id}**

- Change the password of a user system.
- Request Body Format:
```json
{
    "oldPassword": "",
    "newPassword": ""
}
```


### LOGIN
**POST api/v2/auth/login** 
```json
{
    "usernameoremail":"admin@admin.com",
    "password":"Admin#12345"
}
```

**POST api/v2/auth/register** 
- Request Body Format:
```json
    { 
			 "username": "userexample",
			 "dni": "09543895954",
			 "email": "user@example.com",
			 "password": "password#21",
			 "firstname": "User",
			 "lastname": "Example",
			 "img": ""
    }
```
