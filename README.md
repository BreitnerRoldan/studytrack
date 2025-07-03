# StudyTrack
control de actividades

#pasos para ejecutar apis
Nota: Usar Java 21.
1. si el usuario no existe, se debe registrar: localhost:8080/api/users/register   body: "name": "breitner Roldan", "email": "bjroldan@hotmail.com", "password":"Bj1075223523$%"
2. se logea con email y password: localhost:8080/api/users/login  body: "email": "bjroldan@hotmail.com", "password": "Bj1075223523$%"
3. para recuperar contraseña si se olvida: POTSMAN: localhost:8080/api/users/forgot-password  body: "email": "bjroldan@hotmail.com"
- esto te envia una notificacion con enlace al correo q deseas recuperar contraseña;  en postman : formato: x-www-form-urlencoded  key: token = se extrae de la BD : SELECT * FROM password_reset_token = column token, newpassword = nueva contraseña, confirmpassword = confirma la nueva contraseña.
- tambien se puede dar click al enlace de la notificacion q llego al correo, esto te muestra una vista para realizar la actualizacion del password y confirmacion. 
- si por alguna razon se lanza un code=403 se debe crear una contraseña de la aplicacion, osea de Gmail, porq algunas veces la borra y la que se encuentra en el .properties caduca, usar este enlace para crear la contraseña para el correo que envia las notificaciones: https://myaccount.google.com/apppasswords?spm=a2ty_o01.29997173.0.0.7fcbc921XdsvcT&pli=1&rapt=AEjHL4PEKMf6bD_f7tLrj2dhd1Zkx9KqeETm4FYVB_G4dbtnB7KIgGEMnpNoZcIYDmT5JTmWGHiWf12fWtMLMxbO0Q-XUzntPKMW9H1_6qoBX35NJnOAh6U 
- se debe aingresar primero : Bj1075223523 para logear la sesion de Gmail
- se debe tener activo la doble autenticacion en seguridad para generar la contraseña de la aplicacion, la cual permitira el uso del servicio de correo desde la api.
4. localhost:8080/api/users/reset-password : esta api es usada para actualizar el password del usuario. ej: Body: {
    "token": "907bde5d-5e4d-47dc-9d92-44e6e2e29844",
    "newPassword": "Bj1075223523$%.",
    "confirmPassword": "Bj1075223523$%."
}
NOTA: No olvidar activar eL csrf para seguridad de la aplicacion.

correo Gmail:


