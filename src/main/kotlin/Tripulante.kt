import java.time.LocalDate

class Tripulante(): Entidad {
    //Var para el repositorio
    override var id = 0

    // Dejamos fijo que el año siempre sea el 2026
    val anioActual = 2026

    var nombre : String = ""
    var apellido : String = ""
    var fechaNacimiento : LocalDate? = null

    var misionesExitosas : Int = 0
    var misionesParciales : Int = 0
    var misionesFracasadas : Int = 0
    var historialMisiones = mutableListOf<Mision>()

    var salarioBase: Float = 0.0F
    var fechaInicio: LocalDate? = null

    var rol: Rol = SinAsignar() // Antes tenia piloto, ahora tiene este placeholder
    var perfilAptitud: PerfilAptitud = Conformista()

    lateinit var baseAsignada: BaseLanzamiento
    var direccion: Direccion = Direccion()
    var distanciaMaximaCercania: Double = 0.0



    fun salarioTotal() = salarioBase + rol.bonus(this)

    fun edad() = anioActual - fechaNacimiento!!.year

    fun aniosActivo() = anioActual - fechaInicio!!.year

    fun experiencia() = aniosActivo() + (misionesExitosas / 2) + (misionesFracasadas / 2) + (misionesParciales / 4)

    fun condicionesBase() = experiencia() >= 3 && historialMisiones.none { it.estado == EstadoMision.EN_CURSO}

    fun ultimaMisionFueEnCarguero(): Boolean {
        val ultimaCompletada = historialMisiones.lastOrNull { it.estado == EstadoMision.COMPLETADA }
        return ultimaCompletada?.naveAsignada is Carguero
    }

    fun cantidadMisionesFallidas() = historialMisiones.count { it.estado == EstadoMision.FALLIDA }

    fun cantidadPlanetasAterrizados() = historialMisiones.count { it.planetaDestino.fueAterrizado }



    fun esAptoMision(mision: Mision): Boolean {
        return condicionesBase() && perfilAptitud.aceptaMision(mision)
    }

    fun asignarMision (mision: Mision) {
        if (!perfilAptitud.aceptaMision(mision)) {
            throw Exception("El tripiulante no puede aceptar la mision por su perfil de aptitud")
        }
        historialMisiones.add(mision)

    }

    fun registrarMisionExitosa(){ misionesExitosas += 1 }

    fun registrarMisionFallida(){ misionesFracasadas += 1 }

    fun registrarMisionParcial(){ misionesParciales += 1 }

    fun esValido() = nombreValido() && apellidoValido() && fechaNacimientoValida() &&  baseAsignadaValida() && rolValido()

    fun nombreValido()= nombre.isNotEmpty()

    fun apellidoValido()= apellido.isNotEmpty()

    fun fechaNacimientoValida()= fechaNacimiento?.isBefore(LocalDate.now()) == true

    fun baseAsignadaValida() = this::baseAsignada.isInitialized

    fun rolValido() = rol !is SinAsignar

    fun validaPerfilCompuesto(perfiles: List<PerfilAptitud>){
            if (perfiles.size != 2) {
                throw IllegalArgumentException("El PerfilCompuestoTodos debe recibir exactamente 2 perfiles")

        }
    }


    // Metodo para la BaseLanzamiento
    fun consideraCercana(base: BaseLanzamiento): Boolean {
            return this.direccion.estaEnRadio(base.direccion, distanciaMaximaCercania)
        }

    //Metodo para el respositorio
    override fun condicionBusqueda(value: String) =
        nombre.contains(value) || apellido == value
}
// -------------------------------------------------------
// ROLES
    
interface Rol {
    fun bonus(tripulante: Tripulante): Float
}

// Es un rol placeholder, asi no arrancan como pilotos todos
class SinAsignar : Rol {
    override fun bonus(tripulante: Tripulante): Float = 0F
}

class Comandante : Rol {
    override fun bonus(tripulante: Tripulante): Float {
        val fallidas = tripulante.cantidadMisionesFallidas()
        return (tripulante.salarioBase * 0.5F) + (tripulante.salarioBase * 0.05F * fallidas)
    }
}

class Piloto : Rol{
    override fun bonus(tripulante: Tripulante): Float {
        return tripulante.salarioBase * 0.3F
    }
}

class Ingeniero : Rol {
    override fun bonus(tripulante: Tripulante): Float {
        val multiplicador = if (tripulante.ultimaMisionFueEnCarguero()) 0.40F else 0.20F
        return tripulante.salarioBase * multiplicador
    }
}

class Cientifico: Rol {
    override fun bonus(tripulante: Tripulante): Float{
        val planetasAterrizados = tripulante.cantidadPlanetasAterrizados()
        return tripulante.salarioBase * 0.10F * planetasAterrizados
    }

}

class Medico: Rol {
    override fun bonus(tripulante: Tripulante): Float {
        val fallidas = tripulante.cantidadMisionesFallidas()
        return (tripulante.salarioBase * 0.25F) + (tripulante.salarioBase * 0.02F * fallidas)
    }
}

// PERFILES APTITUD

interface PerfilAptitud {
    fun aceptaMision(mision: Mision): Boolean
}

class Conformista : PerfilAptitud {
    override fun aceptaMision(mision: Mision) = true
}

class Exigente : PerfilAptitud {
    override fun aceptaMision(mision: Mision) = mision.naveAsignada.esModerna()
}

class Veterano(val maxDias: Int) : PerfilAptitud {
    override fun aceptaMision(mision: Mision) = mision.duracionEstimada() <= maxDias
}

class Prudente : PerfilAptitud {
    override fun aceptaMision(mision: Mision): Boolean {
        return mision.planetaDestino.idealHumano() && mision.planetaDestino.gravedadSoportable()
    }
}

class Cauteloso(val umbral: Int) : PerfilAptitud {
    override fun aceptaMision(mision: Mision) = mision.planetaDestino.nivelRadiacion < umbral
}

class Explorador : PerfilAptitud {
    override fun aceptaMision(mision: Mision) = !mision.planetaDestino.fueAterrizado
}

class Temerarios : PerfilAptitud {
    override fun aceptaMision(mision: Mision) = !mision.planetaDestino.esHabitable() or mision.esAltoRiesgo()
}


abstract class PerfilCompuesto (val perfiles: List<PerfilAptitud>): PerfilAptitud {
    init{
        require(perfiles.size == 2){"Perfil compuesto debe recibir solo 2 aptitudes"}
    }
}

class PerfilCompuestoTodos(perfiles: List<PerfilAptitud>) : PerfilCompuesto(perfiles){ // PerfilCompuestoAnd

    override fun aceptaMision(mision: Mision) = perfiles.all { it.aceptaMision(mision) }
}

class PerfilCompuestoAlguno(perfiles: List<PerfilAptitud>) : PerfilCompuesto(perfiles) { //PerfilCompuestoOr

    override fun aceptaMision(mision: Mision) = perfiles.any { it.aceptaMision(mision) }
}

class PerfilCambiante(val tripulante: Tripulante) : PerfilAptitud {
    fun edadPar()=(tripulante.edad() % 2 == 0)

    override fun aceptaMision(mision: Mision): Boolean {
        return if  (edadPar()){
            Temerarios().aceptaMision(mision)
        } else {
            Prudente().aceptaMision(mision)
        }
    }
}

