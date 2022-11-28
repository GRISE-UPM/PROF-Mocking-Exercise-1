## Ejercicio 1

### Obtenido de: https://books.google.es/books?id=kas_zAEACAAJ.

**Se pide:** Implementar las siguientes pruebas sobre la clase ``SubscriptionService``, utilizando *mocks*:

*   **State tests**

*   No se puede añadir un ``Client null`` a la lista ``subscribers``.
*   Al añadir un ``Client``mediante ``addSubscriber()``, éste ``Client`` se almacena en la lista ``subscribers``.
*   No se puede añadir dos veces el mismo ``Client`` mediante ``addSubscriber()`` a la lista ``subscribers`` Al hacerlo, se lanza la excepción ``ExistingClientException``.
*   Al añadir varios ``Client`` mediante ``addSubscriber()``, todos los ``Client``se almacenan en la lista ``subscribers``.
*   No se puede eliminar (usando ``removeSubscriber()`` un ``Client null`` de la lista ``subscribers``. Al hacerlo, se lanza la excepción ``NullClientException``.
*   No se puede eliminar (usando ``removeSubscriber()`` un ``Client`` que no está almacenado en la lista ``subscribers``. Al hacerlo, se lanza la excepción ``NonExistingClientException``.
*   Se puede eliminar correctamente (usando ``removeSubscriber()`` un ``Client`` almacenado en la lista ``subscribers``.
*   No se puede eliminar (usando ``removeSubscriber()`` dos veces el mismo ``Client`` de la lista ``subscribers``. Al hacerlo, se lanza la excepción ``NonExistingClientException``.
*   Se pueden eliminar correctamente (usando ``removeSubscriber()`` varios ``Client`` almacenados en la lista ``subscribers``.
*   Se pueden eliminar correctamente (usando ``removeSubscriber()`` todos los ``Client`` almacenados en la lista ``subscribers``.

*   **Interaction tests**

*   Un ``Client`` suscrito recibe mensajes (método ``receiveMessage()`` si tiene email (método ``hasEmail() == true``).
*   Un ``Client`` suscrito no recibe mensajes (método ``receiveMessage()`` si no tiene email (método ``hasEmail() == false``).
*   Varios  ``Client`` suscritos reciben mensajes (método ``receiveMessage()`` si tienen email (método ``hasEmail() == true``).
*   Al des-suscribir un ``Client`` éste no recibe mensajes (método ``receiveMessage()``).

**Se entregará:** Debéis realizar un pull request al repositorio. Indicad vuestro nombre y apellidos en el comentario.
