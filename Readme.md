# Comparador de Palabras mediante Sockets UDP

Este proyecto implementa una arquitectura **Cliente-Servidor** en Java utilizando el protocolo **UDP** (`DatagramSocket` y `DatagramPacket`). El cliente envía una lista de palabras al servidor y este evalúa cuál tiene mayor longitud o si existe un empate entre ellas.

---

## Formato de Envío de Datos

Para la comunicación entre el cliente y el servidor se ha definido un protocolo de texto plano sencillo y ligero basado en **valores separados por comas (CSV en línea)**:

```text
palabra1,palabra2[,palabra3,...]
```

### Características del formato:
1. **Delimitador:** Se utiliza el carácter coma (`,`) para separar cada una de las palabras dentro de un único datagrama UDP. Esto permite enviar todas las palabras en un solo paquete (`1024 bytes` máximo), evitando problemas de desorden o pérdida de paquetes individuales propios de UDP.
2. **Codificación:** Todos los mensajes (tanto peticiones como respuestas) se codifican en **`UTF-8`** (`StandardCharsets.UTF_8`), garantizando el soporte correcto para tildes, la letra `ñ` y caracteres especiales.
3. **Limpieza de espacios:** El servidor aplica `.trim()` a cada elemento tras dividir la cadena con `.split(",")`. Por tanto, formatos con o sin espacios alrededor de la coma son igualmente válidos:
    * `casa,elefante` $\rightarrow$ Válido
    * `casa, elefante` $\rightarrow$ Válido
4. **Cantidad de palabras:** Aunque el cliente solicita dos palabras al usuario, el protocolo del servidor admite **dos o más palabras** separadas por comas.

---

## Reglas de Validación y Respuestas del Servidor

Al recibir un datagrama en el puerto `5555`, el servidor procesa la cadena y devuelve uno de los siguientes resultados:

| Caso | Ejemplo de entrada | Respuesta del servidor |
| :--- | :--- | :--- |
| **Ganadora única** | `sol, ordenador` | `La palabra más larga es "ordenador" y tiene 9 caracteres.` |
| **Empate de longitud** | `gato, perro, casa` | `Tienen la misma longitud (5 caracteres).` *(si empata con la máxima)* |
| **Menos de 2 palabras** | `hola` | `Formato incorrecto. Envía al menos dos palabras separadas por comas.` |
| **Elementos vacíos** | `hola, , mundo` | `Formato incorrecto. Envía palabras separadas por comas.` |

---

## Instrucciones de Ejecución

1. **Iniciar el Servidor:**
   Ejecuta primero la clase `ServidorUDP`. Quedará a la escucha en `localhost:5555`.
2. **Iniciar el Cliente:**
   Ejecuta la clase `ClienteUDP`. Introduce las palabras separadas por comas cuando se solicite, o escribe `salir` para cerrar la aplicación.
   *(Nota: El cliente dispone de un tiempo de espera máximo o timeout de **2 segundos** para recibir respuesta del servidor).*