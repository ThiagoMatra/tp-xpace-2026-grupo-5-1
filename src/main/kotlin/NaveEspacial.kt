import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.times


abstract class NaveEspacial: Entidad {
    //Var para el repositorio
    override var id = 0

    var nombre : String = ""
    var codigoID : String = ""
    var fechaFabricacion : LocalDate? = null
    var velocidadPromedio : Float = 0.0F
    var autonomia : Int = 0 // Dias que puede operar sin reabastecerse
    var consumoBase : Double = 0.0 // Litros por light year
    var enMision : Boolean = false
    var capacidadTripulantes : Int = 0
    //val tripulantes = mutableListOf<Tripulante>() // Esta lista se paso a Mision

    abstract fun consumoNave(cantidadTripulantes: Int = 0) : Double // Template method, obligamos que se declare

    fun desasignarMision() {enMision = false}

    fun asignarMision() {enMision = true}

    fun consumoTotal(planeta: Planeta, cantidadTripulantes: Int = 0) = consumoNave(cantidadTripulantes) * planeta.distanciaTierra

    fun antiguedad() = LocalDateTime.now().year - fechaFabricacion!!.year

    fun tiempoDeViaje(planeta: Planeta) = planeta.distanciaTierra * 365 / velocidadPromedio * 2

    fun puedeAlcanzar(planeta: Planeta) = tiempoDeViaje(planeta) <= autonomia

    fun esModerna() = antiguedad() < 5

    // Metodos validacion
    fun nombreValido() = nombre.isNotEmpty()
    fun codigoValido() = codigoID.isNotEmpty()
    fun velocidadValido() = velocidadPromedio > 0
    fun autonomiaValido() = autonomia > 0
    fun consumoValido() = consumoBase > 0

    fun esValido() = nombreValido() && codigoValido() && velocidadValido() && autonomiaValido() && consumoValido()

    abstract fun esAptoMision(planeta: Planeta, cantidadTripulantes: Int = 0) : Boolean

    //Metodo para el respositorio
    override fun condicionBusqueda(value: String) =
        codigoID == value
}

class Sonda : NaveEspacial(){

    override fun consumoNave(cantidadTripulantes: Int) = consumoBase
    override fun esAptoMision(planeta: Planeta, cantidadTripulantes: Int) = puedeAlcanzar(planeta) && !enMision && cantidadTripulantes == 0
}

class Transobrador : NaveEspacial(){

    override fun esAptoMision(planeta: Planeta, cantidadTripulantes: Int) = puedeAlcanzar(planeta) && !enMision && capacidadTripulantes >= cantidadTripulantes
    override fun consumoNave(cantidadTripulantes: Int) = consumoBase + consumoBase * (cantidadTripulantes * 0.1)
}

class Carguero : NaveEspacial(){
    var capacidadCarga: Int = 0  // Capaz estas variables se suben a la padre, no creo
    var carga: Int = 0


    fun consumoCarguero() = consumoBase + (consumoBase * (0.05 * carga ))

    override fun esAptoMision(planeta: Planeta, cantidadTripulantes: Int) = puedeAlcanzar(planeta) && !enMision
    override fun consumoNave(cantidadTripulantes: Int) = if (antiguedad() < 10) consumoCarguero() else consumoCarguero() * 1.2

}