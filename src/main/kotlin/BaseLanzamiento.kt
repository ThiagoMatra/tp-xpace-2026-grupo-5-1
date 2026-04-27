import org.uqbar.geodds.Point

class BaseLanzamiento: Entidad {

    //Var para el repositorio
    override var id = 0

    var nombre : String = ""
    val navesEstacionadas = mutableListOf<NaveEspacial>()
    var capacidadNaves : Int = 0
    var direccion : Direccion = Direccion()

    //Metodo para el respositorio
    override fun condicionBusqueda(value: String) =
        nombre.contains(value) || direccion.pais == value




}

class Direccion{
    var pais : String = ""
    var ciudad : String = ""
    var calle : String = ""
    var altura : Int = 0
    var ubicacion : Point = Point(0.0, 0.0)

    fun estaEnRadio(otraDireccion: Direccion, radioMaximo: Double): Boolean {
        return this.ubicacion.distance(otraDireccion.ubicacion) <= radioMaximo
    }

}