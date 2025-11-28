# API Kardex

Este proyecto implementa un backend para la gestión de inventario multiempresa. A continuación se describe el flujo típico de uso de los endpoints, el manejo de errores y cómo consultar la documentación interactiva en Swagger.

## Flujo de los endpoints

1. **Autenticación**
   - `POST /api/auth/registro`: registra un usuario y retorna el token JWT inicial.
   - `POST /api/auth/login`: genera un token JWT para acceder a los endpoints protegidos.

2. **Habilitación de la empresa y sus sedes**
   - `POST /api/companies`: crea la empresa (solo `GLOBAL_ADMIN`).
   - `POST /api/sedes`: registra sedes asociadas a la empresa.
   - `PATCH /api/companies/{id}/status`: activa o inactiva la empresa y sus sedes.

3. **Configuración de inventario**
   - `POST /api/inventory/config` y `PUT /api/inventory/config`: establecen o actualizan políticas base de inventario para la empresa.

4. **Productos**
   - `POST /api/products` (`/api/productos`): crea un producto nuevo validando SKU único y sede asociada.
   - `PUT /api/products/{id}` / `PATCH /api/products/{id}`: actualiza el producto completo o parcialmente.
   - `DELETE /api/products/{id}`: inactiva el producto solo si no tiene stock disponible.
   - `GET /api/products`: consulta paginada con filtros por nombre o SKU.

5. **Movimientos y ajustes**
   - `POST /api/movements`: registra compras o ventas, recalculando costos promedio y actualizando stock en la sede indicada.
   - `GET /api/products/{id}/movements`: lista los movimientos de un producto, filtrando opcionalmente por sede.
   - `POST /api/adjustments`: crea ajustes positivos o negativos (mermas/sobrantes) y registra la auditoría correspondiente.

6. **Transferencias entre sedes**
   - `POST /api/transfers/initiate`: descuenta stock en la sede origen y deja la transferencia en estado *IN_TRANSIT*.
   - `POST /api/transfers/{id}/confirm`: incrementa el stock en la sede destino y marca la transferencia como *COMPLETED*.

7. **Reportes y consultas adicionales**
   - `GET /api/reports/low-stock`: obtiene productos con stock por debajo del mínimo.
   - `GET /api/companies/{id}/sedes` y `GET /api/companies/{id}/users`: navega la información vinculada a empresas activas.

## Manejo de errores

El controlador global devuelve respuestas homogéneas mediante el esquema `ApiError`:

```json
{
  "status": 400,
  "mensaje": "Detalle legible del problema",
  "timestamp": "2024-05-01T12:00:00",
  "detalles": {
    "campo": "Mensaje de validación opcional"
  }
}
```

- Se responden **400 Bad Request** para validaciones de negocio o cuerpo mal formado.
- Se responden **404 Not Found** cuando un recurso (producto, sede o transferencia) no existe.
- Se responde **403 Forbidden** si el usuario autenticado carece de permisos.
- Cualquier error no controlado devuelve **500 Internal Server Error** con mensaje genérico.

## Documentación Swagger

La configuración de OpenAPI expone la UI en `/swagger-ui.html`. Los endpoints incluyen ahora respuestas de error tipadas con `ApiError` para los códigos 400, 403 y 404, por lo que la especificación muestra los casos de validación, falta de permisos y recursos inexistentes junto a los ejemplos de éxito.
