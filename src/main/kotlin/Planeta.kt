import java.time.LocalDate
import kotlin.math.roundToInt


class Planeta() : Entidad {
    //Var para el repositorio
    override var id = 0

    var nombre: String = ""
    var tamanio : Float = 1.0F
    var gravedad : Float = 1.0F
    var toxicidadAtmosfera : Float = 0.0F
    var nivelRadiacion : Float= 0.0F
    var temperaturaMedia : Float = 0.0F
    var hayAguaLiquida : Boolean = false
    var actividadTectonica : Float = 0.0F
    var fechaDescubrimiento : LocalDate? = null
    var distanciaTierra : Float = 0.0F
    var fueAterrizado : Boolean = false



    fun idealHumano() = temperaturaMedia in 0.0F..40.0F

    fun gravedadSoportable() = gravedad in 3.0F..15.0F

    fun toxicidadSoportable() = toxicidadAtmosfera < 30.0F

    fun radiacionSoportable() = nivelRadiacion < 40.0F

    fun esHabitable() = idealHumano() && gravedadSoportable() && hayAguaLiquida && toxicidadSoportable() && radiacionSoportable()

    fun esExplorable() = !esHabitable() && indicePeligrosidad()<60.0F

    fun indicePeligrosidad() = (nivelRadiacion + toxicidadAtmosfera + actividadTectonica) / 3

    fun registrarAterrizaje() {fueAterrizado = true}

    fun esValido()= nombreValido() && tamanioValido() && gravedadValida() && toxicidadValida() && radiacionValdia()

    fun radiacionValdia(): Boolean = nivelRadiacion in 0.0F..100.0F

    fun toxicidadValida(): Boolean = toxicidadAtmosfera in 0.0F..100.0F

    fun nombreValido() : Boolean = nombre.isNotEmpty()
    
    fun tamanioValido() : Boolean = tamanio > 0
    
    fun gravedadValida() : Boolean = gravedad > 0

    //Metodo para el respositorio
    override fun condicionBusqueda(value: String) =
        nombre.contains(value)

}