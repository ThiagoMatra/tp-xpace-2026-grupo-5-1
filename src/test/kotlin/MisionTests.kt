import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate
import kotlin.collections.emptyList

class MisionesTests : DescribeSpec({

    describe("Test de Misiones") {

        it("Deberia tener 73 de duraicon") {
            val misionDuracion = Mision().apply {
                nombre = "Facilongui"
                naveAsignada = Sonda().apply{
                    velocidadPromedio = 100F
                }
//                tripulantes = ["Manu", "Lean"]
                planetaDestino = Planeta().apply{
                    distanciaTierra = 10F
                }
            }
            misionDuracion.duracionEstimada() shouldBe 73F
        }

        it("Deberia lanzar mision con 2 tripulantes en Transbordador y completar mision") {
            val transMision = Mision().apply {
                nombre = "Multiples tripulantes"
                naveAsignada = Transobrador().apply{
                    nombre = "Transobrador"
                    velocidadPromedio = 100F
                    autonomia = 10000
                    capacidadTripulantes = 3
                }
                planetaDestino = Planeta().apply{
                    distanciaTierra = 10F
                }
            }
            transMision.esAltoRiesgo() shouldBe false
            val tripulanteManu = Tripulante().apply{
                nombre = "Manu"
                baseAsignada = BaseLanzamiento().apply{
                    nombre = "Base de datos"
                }
                fechaInicio = LocalDate.of(2018, 12, 9)
            }
            val tripulanteEnzo = Tripulante().apply{
                nombre = "Enzo"
                baseAsignada = BaseLanzamiento().apply{
                    nombre = "Base de datos"
                }
                fechaInicio = LocalDate.of(2018, 12, 9)
            }
            transMision.registrarTripulante(tripulanteManu)
            transMision.registrarTripulante(tripulanteEnzo)
            transMision.tripulantesAptos() shouldBe true
            transMision.baseApta() shouldBe true
            transMision.estadoApto() shouldBe true
            transMision.naveApta() shouldBe true
            transMision.misionAptaLanzamiento() shouldBe true
            transMision.lanzarMision()
            transMision.completarMision()
            transMision.estado shouldBe EstadoMision.COMPLETADA
            tripulanteManu.misionesExitosas shouldBe 1
        }

        it("Deberia fallar mision") {
            val tripulanteManu = Tripulante().apply{
                nombre = "Manu"
            }
            val misionFallida = Mision().apply {
                nombre = "Mision failll"
                naveAsignada = Transobrador().apply{
                    nombre = "Transou"
                    velocidadPromedio = 100F
                    autonomia = 10000
                }
                planetaDestino = Planeta().apply{
                    distanciaTierra = 10F
                }
                fechaLanzamiento = LocalDate.of(2025, 12, 9)
            }
            misionFallida.registrarTripulante(tripulanteManu)
            misionFallida.lanzarMision()
            misionFallida.misionFallida()
            misionFallida.estado shouldBe EstadoMision.FALLIDA
            tripulanteManu.misionesFracasadas shouldBe 1
        }

        it("Deberia cancelar mision de alto riesgo en curso") {
            val tripulanteManu = Tripulante().apply{
                nombre = "Manu"
            }
            val misionFallida = Mision().apply {
                nombre = "Mision en curso cancelada"
                naveAsignada = Transobrador().apply{
                    nombre = "tranx"
                    velocidadPromedio = 100F
                    autonomia = 10000
                }
                planetaDestino = Planeta().apply{
                    distanciaTierra = 740F
                    temperaturaMedia = 150F
                    gravedad = 70F
                }
                fechaLanzamiento = LocalDate.of(2025, 12, 9)
            }
            misionFallida.registrarTripulante(tripulanteManu)
            misionFallida.lanzarMision()
            misionFallida.cancelarMisionEnCurso()
            misionFallida.estado shouldBe EstadoMision.CANCELADA
            tripulanteManu.misionesParciales shouldBe 1
        }

        it("Deberia cancelar mision de bajo riesgo curso") {
            val tripulanteManu = Tripulante().apply{
                nombre = "Manu"
            }
            val misionFallidaBajoRiesgo = Mision().apply {
                nombre = "Mision en curso cancelada"
                naveAsignada = Transobrador().apply{
                    nombre = "tranx"
                    velocidadPromedio = 100F
                    autonomia = 10000
                }
                planetaDestino = Planeta().apply{
                    distanciaTierra = 10F
                    temperaturaMedia = 20F
                    gravedad = 5F
                }
                fechaLanzamiento = LocalDate.of(2025, 12, 9)
            }
            misionFallidaBajoRiesgo.registrarTripulante(tripulanteManu)
            misionFallidaBajoRiesgo.lanzarMision()
            misionFallidaBajoRiesgo.cancelarMisionEnCurso()
            misionFallidaBajoRiesgo.estado shouldBe EstadoMision.CANCELADA
            tripulanteManu.misionesParciales shouldBe 0
        }

        it("Deberia no ser apta por distintas bases") {
            val basesDistinas = Mision().apply {
                nombre = "Distintas Bases"
                naveAsignada = Transobrador().apply{
                    nombre = "Transobrador"
                    velocidadPromedio = 100F
                    autonomia = 10000
                }
                planetaDestino = Planeta().apply{
                    distanciaTierra = 10F
                }
            }
            val tripulanteManu = Tripulante().apply{
                nombre = "Manu"
                baseAsignada = BaseLanzamiento().apply{
                    nombre = "Base Bolivia"
                }
            }
            val tripulanteEnzo = Tripulante().apply{
                nombre = "Enzo"
                baseAsignada = BaseLanzamiento().apply{
                    nombre = "Base Jamaica"
                }
            }
            basesDistinas.registrarTripulante(tripulanteManu)
            basesDistinas.registrarTripulante(tripulanteEnzo)
            basesDistinas.tienenDistintaBase() shouldBe true
        }

        it("Deberia no ser apta por tener tripulantes en Sonda") {
            val sondaCargado = Mision().apply {
                nombre = "Sonda charged"
                naveAsignada = Sonda().apply{
                    nombre = "Sondex"
                    velocidadPromedio = 100F
                    autonomia = 10000
                }
                planetaDestino = Planeta().apply{
                    distanciaTierra = 10F
                }
            }
            val tripulanteManu = Tripulante().apply{
                nombre = "Manu"
                baseAsignada = BaseLanzamiento().apply{
                    nombre = "Base Bolivia"
                }
            }
            sondaCargado.registrarTripulante(tripulanteManu)
            sondaCargado.naveApta() shouldBe false
        }
    }

    describe("Test unitarios Validacion") {
        val misionUnitaria = Mision().apply{
            naveAsignada = Transobrador().apply{}
        }
        it("Deberia ser valida por nave asignada (es obligatorio por lateinit)"){
            misionUnitaria.hayNaveAsignada() shouldBe true
        }
        it("Deberia ser invalida por nombre vacio"){
            misionUnitaria.nombreNoVacio() shouldBe false
        }
        it("Deberia ser invalida por nave sin nombre"){
            misionUnitaria.naveTieneNombre() shouldBe false
        }
        it("Deberia ser invalida por planeta sin nombre"){
            misionUnitaria.planetaTieneNombre() shouldBe false
        }
        it("Deberia ser invalida por fecha invalida"){
            misionUnitaria.fechaLanzamientoValida() shouldBe false
        }
        it("Deberia no lanzarse por no tener nave asignada"){
            misionUnitaria.liberarNave()
            misionUnitaria.naveApta() shouldBe false
        }
        it("Deberia no lanzarse por tener estado erroneo"){
            misionUnitaria.estado = EstadoMision.FALLIDA
            misionUnitaria.estadoApto() shouldBe false
        }
        it("Deberia ser inválida"){
            misionUnitaria.esValido() shouldBe false
        }
    }

    describe("Test de presupuesto de mision") {
        val misionPresupuestada = Mision().apply {
            nombre = "misionGuita"
            naveAsignada = Sonda().apply {
                velocidadPromedio = 100F
                fechaFabricacion = LocalDate.of(2024, 6, 22)
                consumoBase = 10.0
            }
            planetaDestino = Planeta().apply {
                distanciaTierra = 10F
                gravedad = 10F

            }
        }
            misionPresupuestada.subvencion = Subvencion().apply {
                fechaEmision = LocalDate.now().minusMonths(1)
                duracion = 60
                montoBase = 1000F
                tipo = DeEmergencia()
            }

        it("Deberia no agregar mantenimiento de nave moderna") {
            misionPresupuestada.naveNoModerna() shouldBe 1F
        }
        it("Recargo por riesgo deberia activarse") {
            misionPresupuestada.recargoPorRiesgo() shouldBe 1.20F
        }
        it("Costo de combustible deberia ser 5000") {
            misionPresupuestada.costoCombustible() shouldBe 5000.0
        }
        it("Costo de salarios deberia ser 0") {
            misionPresupuestada.costoSalarial() shouldBe 0F
        }
        it("Deberia tener 6000 de presupuesto fijo") {
            misionPresupuestada.presupuestoFijo() shouldBe 6000F
        }
        it("Deberia tener 4000 subvencion"){
            misionPresupuestada.subvencionAplicada() shouldBe 4000F
        }

       it("Deberia tener 2000 de presupuesto total"){
            misionPresupuestada.presupuesto() shouldBe 2000F
       }
    }

})
