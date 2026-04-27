import java.time.LocalDate
import java.time.LocalDateTime

class Subvencion() : Entidad {
    //Var para el repositorio
    override var id = 0

    var nombre: String = ""
    //var fechaEmision: LocalDate? = null
    var fechaEmision = LocalDate.now()
    var duracion: Long = 0
    var montoBase: Float = 0.0f
    var aplicada: Boolean = false
    var tipo: Tipo = SinTipo()

    //Metodo para el respositorio
    override fun condicionBusqueda(value: String) =
        nombre.startsWith(value, ignoreCase = true)

    fun vencida() = fechaEmision.plusDays(duracion).isBefore(LocalDate.now())

    fun montoSubvencion(misionSubvencionada: Mision) = montoBase + tipo.montoAdicional(this, misionSubvencionada)

    fun montoAplicable(misionSubvencionada: Mision) = montoSubvencion(misionSubvencionada) <= misionSubvencionada.presupuestoFijo()

    fun esAplicable(misionSubvencionada: Mision) = !vencida() && !aplicada && montoAplicable(misionSubvencionada) && tipo.condicionAplicable(this, misionSubvencionada)

    fun aplicar(){
        aplicada = true
    }
}

interface Tipo{
    fun montoAdicional(subvencion: Subvencion, misionSubvencionada: Mision): Float
    fun condicionAplicable(subvencion: Subvencion, misionSubvencionada: Mision): Boolean
}

class SinTipo: Tipo{
    override fun montoAdicional(subvencion: Subvencion, misionSubvencionada: Mision): Float = 0f
    override fun condicionAplicable(subvencion: Subvencion, misionSubvencionada: Mision): Boolean = true
}

class Gubernamental: Tipo {
    override fun montoAdicional(subvencion: Subvencion, misionSubvencionada: Mision): Float {
        val porcentaje = if (misionSubvencionada.planetaDestino.esHabitable()) 0.3f else 0.15f
        return subvencion.montoBase * porcentaje
    }

    override fun condicionAplicable(subvencion: Subvencion, misionSubvencionada: Mision): Boolean {
        return !misionSubvencionada.planetaDestino.fueAterrizado
    }
}

class Privada: Tipo{
    override fun montoAdicional(subvencion: Subvencion, misionSubvencionada: Mision): Float {
        val cantidad = misionSubvencionada.tripulantesCon10AniosDeExperiencia().size
        return minOf(cantidad * 10000f, 50000f)
    }
    override fun condicionAplicable(subvencion: Subvencion, misionSubvencionada: Mision) = misionSubvencionada.promedioExperiencia() > 5
}

class DeEmergencia: Tipo {
    override fun montoAdicional(subvencion: Subvencion, misionSubvencionada: Mision) = misionSubvencionada.presupuestoFijo() * 0.5f
    override fun condicionAplicable(subvencion: Subvencion, misionSubvencionada: Mision): Boolean = true
}
