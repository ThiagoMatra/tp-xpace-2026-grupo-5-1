interface Entidad {
    var id: Int
    fun condicionBusqueda(value: String): Boolean
}

class Repositorio<T : Entidad> {
    val coleccion: MutableList<T> = mutableListOf()
    var nextId: Int = 1

    fun create(obj: T) {
        obj.id = nextId++
        coleccion.add(obj)
    }

    fun delete(obj: T) = coleccion.remove(obj)

    fun getById(id: Int): T {
        return coleccion.find { it.id == id } ?: throw Exception("No existe objeto con id $id")
    }

    fun update(obj: T) {
        getById(obj.id)
        coleccion[coleccion.indexOfFirst { it.id == obj.id }] = obj
    }


    fun search(value: String) = coleccion.filter { it.condicionBusqueda(value) }
}





