const API_URL = "/api/productos";

const form = document.getElementById("form-producto");
const idInput = document.getElementById("producto-id");
const nombreInput = document.getElementById("nombre");
const precioInput = document.getElementById("precio");
const stockInput = document.getElementById("stock");
const categoriaInput = document.getElementById("categoria");
const mensajeError = document.getElementById("mensaje-error");
const cuerpoTabla = document.getElementById("cuerpo-tabla");
const btnCancelar = document.getElementById("btn-cancelar");
const btnGuardar = document.getElementById("btn-guardar");

async function cargarProductos() {
    const respuesta = await fetch(API_URL);
    const productos = await respuesta.json();
    renderizarTabla(productos);
}

function renderizarTabla(productos) {
    cuerpoTabla.innerHTML = "";

    productos.forEach((producto) => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td>${producto.nombre}</td>
            <td>$${producto.precio.toLocaleString("es-CO")}</td>
            <td>${producto.stock}</td>
            <td>${producto.categoria}</td>
            <td>
                <button class="editar" data-id="${producto.id}">Editar</button>
                <button class="eliminar" data-id="${producto.id}">Eliminar</button>
            </td>
        `;

        cuerpoTabla.appendChild(fila);
    });

    document.querySelectorAll(".editar").forEach((btn) => {
        btn.addEventListener("click", () => iniciarEdicion(btn.dataset.id));
    });

    document.querySelectorAll(".eliminar").forEach((btn) => {
        btn.addEventListener("click", () => eliminarProducto(btn.dataset.id));
    });
}

async function iniciarEdicion(id) {
    const respuesta = await fetch(`${API_URL}/${id}`);
    if (!respuesta.ok) {
        return;
    }
    const producto = await respuesta.json();

    idInput.value = producto.id;
    nombreInput.value = producto.nombre;
    precioInput.value = producto.precio;
    stockInput.value = producto.stock;
    categoriaInput.value = producto.categoria;

    btnGuardar.textContent = "Actualizar producto";
    btnCancelar.hidden = false;
}

function limpiarFormulario() {
    form.reset();
    idInput.value = "";
    btnGuardar.textContent = "Guardar producto";
    btnCancelar.hidden = true;
    mensajeError.textContent = "";
}

async function eliminarProducto(id) {
    const confirmado = confirm("¿Seguro que deseas eliminar este producto?");
    if (!confirmado) {
        return;
    }

    await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    cargarProductos();
}

form.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    mensajeError.textContent = "";

    const producto = {
        nombre: nombreInput.value.trim(),
        precio: Number(precioInput.value),
        stock: Number(stockInput.value),
        categoria: categoriaInput.value.trim(),
    };

    const id = idInput.value;
    const esActualizacion = Boolean(id);

    const respuesta = await fetch(esActualizacion ? `${API_URL}/${id}` : API_URL, {
        method: esActualizacion ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto),
    });

    if (!respuesta.ok) {
        const error = await respuesta.json();
        mensajeError.textContent = error.error || "Ocurrió un error al guardar el producto";
        return;
    }

    limpiarFormulario();
    cargarProductos();
});

btnCancelar.addEventListener("click", limpiarFormulario);

cargarProductos();
