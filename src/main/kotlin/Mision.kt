import java.time.LocalDate

// Averigue esta clase muy interesante para obligar a que los estados sean SI O SI estos
// Es decir, si se quiere poner un "completado", el IDE se va a quejar.
enum class EstadoMision {
    BORRADOR,
    EN_CURSO,
    COMPLETADA,
    FALLIDA,
    CANCELADA
}

class Mision: Entidad {
    //Var para el repositorio
    override var id = 0

    var nombre : String = ""
    var descripcion : String = ""
    var fechaLanzamiento : LocalDate = LocalDate.now()
    lateinit var naveAsignada : NaveEspacial
    var planetaDestino : Planeta = Planeta()
    var estado : EstadoMision = EstadoMision.BORRADOR
    private val tripulantes = mutableListOf<Tripulante>()
    var precioCombustible = 500F
    var subvencion : Subvencion = Subvencion()

    // Calculo de duracion estimada de la mision
    fun duracionEstimada() = naveAsignada.tiempoDeViaje(planetaDestino)

    fun registrarTripulante(tripulante: Tripulante) { tripulantes.add(tripulante) }

    // Si la mision es apta para lanzarse, el estado pasa a En Curso
    fun lanzarMision(){
        if (misionAptaLanzamiento()){
            estado = EstadoMision.EN_CURSO
            naveAsignada.asignarMision()
            subvencion.aplicar()
        }
    }

    fun presupuesto() = presupuestoFijo() - subvencionAplicada()

    fun subvencionAplicada() : Float {
        if (subvencion.esAplicable(this)) {
            return subvencion.montoSubvencion(this)
        }
        else return 0F
    }

    fun presupuestoFijo() = (costoCombustible().toFloat() + costoSalarial()) * recargoPorRiesgo() * naveNoModerna()

    fun naveNoModerna() : Float {
        if (!naveAsignada.esModerna()) return 1.10F
        else return 1F
    }

    fun recargoPorRiesgo() : Float {
        if (!planetaDestino.esHabitable()) return 1.20F
        else return 1F
    }

    fun costoCombustible() = naveAsignada.consumoNave(tripulantes.size) * precioCombustible

    fun costoSalarial() = tripulantes.fold(0F) { sumaSalario, tripulante -> sumaSalario + tripulante.salarioTotal() } * (duracionEstimada() / 30)

    fun misionAptaLanzamiento() = estadoApto() && naveApta() && tripulantesAptos() && baseApta()

    fun estadoApto() = estado == EstadoMision.BORRADOR

    fun naveApta() = naveAsignada.esAptoMision(planetaDestino, tripulantes.size)

    fun tripulantesAptos() = tripulantes.all { it.esAptoMision(this) }

    fun baseApta() = !tienenDistintaBase()

    fun tienenDistintaBase() : Boolean {
        if (tripulantes.isEmpty()) return false
        val baseAComparar = tripulantes.first().baseAsignada.nombre
        return tripulantes.any { it.baseAsignada.nombre != baseAComparar }
    }

    fun completarMision(){
        estado = EstadoMision.COMPLETADA
        planetaDestino.registrarAterrizaje()
        registrarMisionesExitosas()
        liberarNave()
    }

    fun misionFallida(){
        estado = EstadoMision.FALLIDA
        registrarMisionesFallidas()
        liberarNave()
    }

    fun cancelarMisionEnCurso(){
        if (esAltoRiesgo()){
            registrarMisionesParciales()
        }
        estado = EstadoMision.CANCELADA
        liberarNave()
    }

    fun liberarNave(){ naveAsignada.desasignarMision() }

    fun esAltoRiesgo() = !planetaDestino.idealHumano() && duracionEstimada() > 500

    // CONDICIONES DE VALIDEZ
    fun esValido() = nombreNoVacio() && hayNaveAsignada() && planetaTieneNombre() && fechaLanzamientoValida()

    fun hayNaveAsignada() = this::naveAsignada.isInitialized

    fun nombreNoVacio() = nombre.isNotBlank()

    fun naveTieneNombre() = naveAsignada.nombre.isNotBlank()

    fun planetaTieneNombre() = planetaDestino.nombre.isNotBlank()

    fun fechaLanzamientoValida() = (fechaLanzamiento.isAfter(LocalDate.now()))

    fun registrarMisionesExitosas() = tripulantes.forEach { it.registrarMisionExitosa() }

    fun registrarMisionesFallidas() = tripulantes.forEach { it.registrarMisionFallida() }

    fun registrarMisionesParciales() = tripulantes.forEach { it.registrarMisionParcial() }

    // Metodos para subvencion
    fun promedioExperiencia() = tripulantes.map { it.experiencia() }.average()

    fun tripulantesCon10AniosDeExperiencia() = tripulantes.map { it.experiencia() > 10 }

    //Metodo para el respositorio
    override fun condicionBusqueda(value: String) =
        nombre == value || planetaDestino.nombre == value

}