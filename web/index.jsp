<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="estilos/login.css">

<title>login</title>
</head>
<body>
    <div class="container">
       
        <div class="login-container">
            <center><div class="logo-container"><img src="logo/dulces.png" alt="Logo ClubAdmi" class="logo"></div></center>
            <h1>Inicia Sesión</h1>
            <form name="formulario" method="post" action="validar.jsp" aria-labelledby="formulario-title" class="form-horizontal">
                    <div class="form-group">
                        <label for="identificacion" class="control-label">Usuario</label>
                        <input type="text" class="form-control" name="identificacion" id="identificacion" required aria-required="true" placeholder="Ingrese su usuario">
                    </div>
                    <div class="form-group">
                        <label for="clave" class="control-label">Contraseña</label>
                        <input type="password" class="form-control" name="clave" id="clave" required aria-required="true" placeholder="Ingrese su contraseña">
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn btn-custom">Iniciar</button>
                    </div>
            </form>
        </div>
    </div>
</body>
</html>
