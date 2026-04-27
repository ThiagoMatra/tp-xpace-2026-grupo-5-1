import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue

interface ServicePlanetas{
    fun getPlanetas() : String
}

data class PlanetaDatos(
    val id: Int? = null,
    val nombre: String,
    val temperaturaMedia: Float,
    val gravedad: Float,
    val nivelRadiacion: Float,
    val hayAguaLiquida: Boolean,
    val toxicidadAtmosfera: Float,
    val actividadTectonica: Float,
    val tamanio: Float,
    val distanciaTierra: Float
)

class ServiceActualizacion(
    private val servicePlanetas: ServicePlanetas,
    private val repositorio: Repositorio<Planeta>
){
    private val objectMapper = jacksonObjectMapper()

    fun actualizar(){
        val json = servicePlanetas.getPlanetas()
        //Data transfer objects, son los objetos del JSON con los que vamos a comparar el repo
        val dtos =  objectMapper.readValue<List<PlanetaDatos>>(json)

        dtos.forEach { planeta ->
            if (planeta.id == null){
                crearPlaneta(planeta)
            }
            else{
                try{
                    val planetaExistente = repositorio.getById(planeta.id)

                    planetaExistente.nombre = planeta.nombre
                    planetaExistente.temperaturaMedia = planeta.temperaturaMedia
                    planetaExistente.gravedad = planeta.gravedad
                    planetaExistente.nivelRadiacion = planeta.nivelRadiacion
                    planetaExistente.hayAguaLiquida = planeta.hayAguaLiquida
                    planetaExistente.toxicidadAtmosfera = planeta.toxicidadAtmosfera
                    planetaExistente.actividadTectonica = planeta.actividadTectonica
                    planetaExistente.tamanio = planeta.tamanio
                    planetaExistente.distanciaTierra = planeta.distanciaTierra

                    repositorio.update(planetaExistente)

                } catch (ex: Exception) {
                    crearPlaneta(planeta)
                }
            }
        }
    }

    private fun crearPlaneta(dto: PlanetaDatos) {
        repositorio.create(Planeta().apply {
            nombre = dto.nombre
            temperaturaMedia = dto.temperaturaMedia
            gravedad = dto.gravedad
            nivelRadiacion = dto.nivelRadiacion
            hayAguaLiquida = dto.hayAguaLiquida
            toxicidadAtmosfera = dto.toxicidadAtmosfera
            actividadTectonica = dto.actividadTectonica
            tamanio = dto.tamanio
            distanciaTierra = dto.distanciaTierra
        })
    }

}




